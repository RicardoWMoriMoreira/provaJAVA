package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.model.enums.UserRole;
import com.ecommerce.service.UserService;

import java.util.Optional;

public class UserController {

    private final UserService userService = new UserService();

    public boolean registerUser(String email, String pwd, String confirm,
                                String name, UserRole role) {
        return userService.registerUser(email, pwd, confirm, name, role);
    }

    public Optional<User> login(String email, String password) {
        return userService.login(email, password);
    }

    public Optional<User> findUserByEmail(String email) {
        return userService.findByEmail(email);
    }

}