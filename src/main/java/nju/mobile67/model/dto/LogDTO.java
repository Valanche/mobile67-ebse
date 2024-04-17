package nju.mobile67.model.dto;

import lombok.Data;
import nju.mobile67.model.entity.Log;

import java.util.List;

@Data
public class LogDTO {

    Long logsId;

    List<Log> logList;
}
