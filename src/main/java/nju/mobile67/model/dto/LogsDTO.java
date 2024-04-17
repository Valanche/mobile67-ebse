package nju.mobile67.model.dto;

import lombok.Data;
import nju.mobile67.model.entity.Logs;

import java.util.List;

@Data
public class LogsDTO {

    List<Logs> logsList;
}
