package nju.mobile67.service.impl;

import lombok.extern.slf4j.Slf4j;
import nju.mobile67.model.dto.LogsDTO;
import nju.mobile67.model.entity.Logs;
import nju.mobile67.model.entity.Type;
import nju.mobile67.model.entity.User;
import nju.mobile67.repository.LogsRepository;
import nju.mobile67.repository.TypeRepository;
import nju.mobile67.repository.UserRepository;
import nju.mobile67.service.LogsService;
import nju.mobile67.service.RedisService;
import nju.mobile67.service.SerializationService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LogsServiceImpl implements LogsService {

    @Autowired
    LogsRepository logsRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    TypeRepository typeRepository;

    @Autowired
    RedisService redisService;

    @Autowired
    SerializationService serializationService;

    @Override
    public Logs saveLogs(Logs logs) {
        return logsRepository.save(logs);
    }

    @Override
    public Optional<Logs> getLogsById(Long id) {
        return logsRepository.findById(id);
    }

    @Override
    public List<Logs> getAllLogs() {
        return logsRepository.findAll();
    }

    @Override
    public void deleteLogs(Long id) {
        logsRepository.deleteById(id);
    }

    @Override
    @Transactional
    public LogsDTO findLogsByUserAndType(String username, String typeName) {
        String key = "logs:" + username + ":" + typeName;
        String cachedLogs = redisService.getLogs(key);

        if (cachedLogs != null) {
            log.info("get logs from cache");
            return serializationService.convertStringToLogsDTO(cachedLogs);
        }
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Type> type = typeRepository.findByName(typeName);

        LogsDTO logsDTO = new LogsDTO();
        if (user.isPresent() && type.isPresent()) {
            List<Logs> logsList = logsRepository.findAllByUserAndType(user.get(), type.get());
            logsDTO.setLogsList(logsList);

        }
        redisService.updateCacheAsync(key, serializationService.convertLogsToString(logsDTO));
        log.info("get logs from database");
        return logsDTO;
    }
}
