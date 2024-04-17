package nju.mobile67.controller;

import io.swagger.annotations.ApiOperation;
import nju.mobile67.model.dto.UserDTO;
import nju.mobile67.model.entity.User;
import nju.mobile67.model.form.LoginForm;
import nju.mobile67.service.UserService;
import nju.mobile67.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public Result<UserDTO> login(@RequestBody LoginForm loginForm) {
        User user = userService.login(loginForm);
        if (user == null) {
            return Result.fail("用户名或密码错误");
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setUsername(user.getUsername());
        return Result.success(userDTO);
    }

    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public Result<UserDTO> register(@RequestBody LoginForm loginForm) {
        User userLogin = userService.login(loginForm);
        if (userLogin != null) {
            return Result.fail("用户名已被注册");
        }
        if (loginForm.getUsername() == null || loginForm.getPassword() == null) {
            return Result.fail("用户名或密码不能为空");
        }
        User user = new User();
        user.setUsername(loginForm.getUsername());
        user.setPassword(loginForm.getPassword());
        user = userService.saveUser(user);
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getId());
        userDTO.setUsername(user.getUsername());
        return Result.success(userDTO);
    }


    @ApiOperation(value = "获取所有用户")
    @GetMapping("/list")
    public Result<List<UserDTO>> list() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTOs.add(userDTO);
        }
        return Result.success(userDTOs);
    }
}
