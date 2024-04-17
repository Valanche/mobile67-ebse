package nju.mobile67.controller;


import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import nju.mobile67.model.dto.LogDTO;
import nju.mobile67.model.form.ChatForm;
import nju.mobile67.service.SparkDeskService;
import nju.mobile67.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spark")
@CrossOrigin
public class ChatController {

    @Autowired
    SparkDeskService sparkDeskService;

    @ApiOperation(value = "大模型消息发送接口")
    @PostMapping("/chat")
    public Result<LogDTO> chat(@RequestBody ChatForm chatForm) {
        LogDTO res = sparkDeskService.chat(chatForm);
        return Result.success(res);
    }
}
