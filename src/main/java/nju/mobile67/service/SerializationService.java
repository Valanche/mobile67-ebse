package nju.mobile67.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import nju.mobile67.model.dto.LogsDTO;
import nju.mobile67.model.entity.Logs;
import org.springframework.stereotype.Service;

@Service
public class SerializationService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String convertLogsToString(LogsDTO logs) {
        try {
            return objectMapper.writeValueAsString(logs);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during JSON serialization", e);
        }
    }

    public LogsDTO convertStringToLogsDTO(String json) {
        try {
            return objectMapper.readValue(json, LogsDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error during JSON deserialization", e);
        }
    }
}
