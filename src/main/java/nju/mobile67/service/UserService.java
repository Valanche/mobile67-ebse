package nju.mobile67.service;

import nju.mobile67.model.entity.User;
import nju.mobile67.model.form.LoginForm;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    Optional<User> getUserById(Long id);
    List<User> getAllUsers();
    void deleteUser(Long id);

    User login(LoginForm loginForm);
}
