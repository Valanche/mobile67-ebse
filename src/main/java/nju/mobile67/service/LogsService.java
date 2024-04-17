package nju.mobile67.service;

import nju.mobile67.model.dto.LogsDTO;
import nju.mobile67.model.entity.Logs;

import java.util.List;
import java.util.Optional;

public interface LogsService {
    Logs saveLogs(Logs logs);
    Optional<Logs> getLogsById(Long id);
    List<Logs> getAllLogs();
    void deleteLogs(Long id);

    LogsDTO findLogsByUserAndType(String username, String typeName);
}
