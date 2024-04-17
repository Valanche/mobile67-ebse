package nju.mobile67.service.impl;

import nju.mobile67.model.entity.Log;
import nju.mobile67.repository.LogRepository;
import nju.mobile67.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    LogRepository logRepository;

    @Override
    public Log saveLog(Log log) {
        return logRepository.save(log);
    }

    @Override
    public Optional<Log> getLogById(Long id) {
        return logRepository.findById(id);
    }

    @Override
    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }

    @Override
    public void deleteLog(Long id) {
        logRepository.deleteById(id);
    }
}
