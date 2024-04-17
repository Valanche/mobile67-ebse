package nju.mobile67.service;

import com.unfbx.sparkdesk.SparkDeskClient;
import com.unfbx.sparkdesk.constant.SparkDesk;
import com.unfbx.sparkdesk.entity.*;
import com.unfbx.sparkdesk.listener.ChatListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import nju.mobile67.model.dto.LogDTO;
import nju.mobile67.model.dto.LogsDTO;
import nju.mobile67.model.entity.Log;
import nju.mobile67.model.entity.Logs;
import nju.mobile67.model.form.ChatForm;
import nju.mobile67.repository.TypeRepository;
import nju.mobile67.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
public class SparkDeskService {

    @Autowired
    LogService logService;

    @Autowired
    LogsService logsService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TypeRepository typeRepository;

    @Value("${spark_desk.api-key}")
    private String apiKey;

    @Value("${spark_desk.api-id}")
    private String appId;

    @Value("${spark_desk.api-secret}")
    private String apiSecret;

    @Value("${spark_desk.max-token}")
    private Integer maxToken;

    @Autowired
    RedisService redisService;

    @Transactional
    public LogDTO chat(ChatForm chatForm) {

        String msg = chatForm.getMessage();
        String username = chatForm.getUsername();
        Long logsId = chatForm.getLogsId();
        String typeName = chatForm.getType();

        StringBuilder response = new StringBuilder();

        // 定义缓存键
        String cacheKey = "chat:" + username + ":" + msg.hashCode();

        // 检查缓存
        String cachedResponse = redisService.getChatResponse(cacheKey);
        if (cachedResponse != null) {
            log.info("大模型使用缓存的响应");
            response.append(cachedResponse);  // 使用缓存的响应
        } else {
            //构建客户端
            log.info("大模型使用新的响应");
            SparkDeskClient sparkDeskClient = SparkDeskClient.builder()
                    .host(SparkDesk.SPARK_API_HOST_WS_V2_1)
                    .appid(appId)
                    .apiKey(apiKey)
                    .apiSecret(apiSecret)
                    .build();
            //构建请求参数
            InHeader header = InHeader.builder().uid(UUID.randomUUID().toString().substring(0, 10)).appid(appId).build();
            Parameter parameter = Parameter.builder().chat(Chat.builder().domain("generalv2").maxTokens(maxToken).temperature(0.3).build()).build();
            List<Text> text = new ArrayList<>();
            text.add(Text.builder().role(Text.Role.USER.getName()).content(msg).build());
            InPayload payload = InPayload.builder().message(Message.builder().text(text).build()).build();
            AIChatRequest aiChatRequest = AIChatRequest.builder().header(header).parameter(parameter).payload(payload).build();


            CountDownLatch countDownLatch = new CountDownLatch(1);

            //发送请求
            sparkDeskClient.chat(new ChatListener(aiChatRequest) {
                //异常回调
                @SneakyThrows
                @Override
                public void onChatError(AIChatResponse aiChatResponse) {
                    log.warn(String.valueOf(aiChatResponse));
                    countDownLatch.countDown(); // 错误时也需要释放锁
                }

                //输出回调
                @Override
                public void onChatOutput(AIChatResponse aiChatResponse) {
                    log.info(String.valueOf(aiChatResponse));
                    if (aiChatResponse != null && aiChatResponse.getPayload() != null) {
                        Choices choices = aiChatResponse.getPayload().getChoices();
                        if (choices != null && choices.getText() != null) {
                            for (Text text : choices.getText()) {
                                if (text.getRole().equalsIgnoreCase("assistant")) {
                                    response.append(text.getContent());
                                }
                            }
                        }
                    }
                }

                //会话结束回调
                @Override
                public void onChatEnd() {
                    System.out.println("当前会话结束了");
                    countDownLatch.countDown(); // 收到响应后释放锁
                }

                //会话结束 获取token使用信息回调
                @Override
                public void onChatToken(Usage usage) {
                    System.out.println("token 信息：" + usage);
                }
            });

            try {
                countDownLatch.await(); // 等待收到响应
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Log logResponse = new Log();
        Log logRequest = new Log();

        logRequest.setValue(msg);
        logResponse.setValue(response.toString());

        logRequest.setDirection(0);
        logResponse.setDirection(1);

        logService.saveLog(logRequest);
        logService.saveLog(logResponse);

        Optional<Logs> logs = logsService.getLogsById(logsId);
        LogDTO resp = new LogDTO();
        if (logs.isPresent()) {
            // 注意双向绑定
            logResponse.setLogs(logs.get());
            logRequest.setLogs(logs.get());

            logs.get().getLogList().add(logRequest);
            logs.get().getLogList().add(logResponse);

            resp.setLogList(logs.get().getLogList());
            logsService.saveLogs(logs.get());
            resp.setLogsId(logs.get().getId());
        } else {
            Logs newLogs = new Logs();

            // 注意双向绑定
            logRequest.setLogs(newLogs);
            logResponse.setLogs(newLogs);

            List<Log> logList = new ArrayList<>();
            logList.add(logRequest);
            logList.add(logResponse);

            resp.setLogList(logList);
            newLogs.setLogList(logList);
            newLogs.setUser(userRepository.findByUsername(username).get());
            newLogs.setType(typeRepository.findByName(typeName).get());

            logsService.saveLogs(newLogs);
            resp.setLogsId(newLogs.getId());
        }

        // 更新缓存
        redisService.cacheChatResponse(cacheKey, response.toString());

        return resp;

    }

}
