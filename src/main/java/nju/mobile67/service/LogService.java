package nju.mobile67.service;

import nju.mobile67.model.entity.Log;

import java.util.List;
import java.util.Optional;

public interface LogService {
    Log saveLog(Log log);
    Optional<Log> getLogById(Long id);
    List<Log> getAllLogs();
    void deleteLog(Long id);
}

