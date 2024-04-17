package nju.mobile67.controller;

import io.swagger.annotations.ApiOperation;
import nju.mobile67.model.dto.LogsDTO;
import nju.mobile67.service.LogsService;
import nju.mobile67.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/logs")
@CrossOrigin
public class LogsController {

    @Autowired
    LogsService logsService;

    @ApiOperation(value = "根据username和type获取聊天记录")
    @GetMapping
    public Result<LogsDTO> getLogsByUserAndType(@RequestParam String username, @RequestParam String type) {
        LogsDTO logs = logsService.findLogsByUserAndType(username, type);
        return Result.success(logs);
    }
}
