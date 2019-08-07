package com.solvd.webappsimple.web.controller;

import com.solvd.webappsimple.domain.User;
import com.solvd.webappsimple.service.UserService;
import com.solvd.webappsimple.web.annotation.PostMapping;
import com.solvd.webappsimple.web.annotation.RequestBody;
import com.solvd.webappsimple.web.annotation.RequestMapping;
import com.solvd.webappsimple.web.dto.LoginModel;
import com.solvd.webappsimple.web.security.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.WEEKS;

@RequestMapping("/auth")
public class AuthController extends BaseController {

    @PostMapping("/login")
    public String login(@RequestBody LoginModel loginModel) {
        UserService userService = getService(UserService.class);
        User foundUser = userService.getByUsername(loginModel.getUsername());

        if (foundUser == null) {
            throw new RuntimeException("Login failed");
        }

        PasswordEncoder passwordEncoder = new PasswordEncoder();
        if (!passwordEncoder.isValid(loginModel.getPassword(), foundUser.getPassword())) {
            throw new RuntimeException("Login failed");
        }

        String sessionToken = UUID.randomUUID().toString();
        LocalDateTime expiredIn = LocalDateTime.now().plus(2, WEEKS);
        userService.updateSessionId(sessionToken, expiredIn, foundUser.getId());

        return sessionToken;
    }

    @PostMapping("/signup")
    public void signup(@RequestBody User user) {
        if (user.getPassword() == null) {
            throw new RuntimeException("Password required");
        }

        UserService userService = getService(UserService.class);
        User foundUser = userService.getByUsername(user.getUsername());

        if (foundUser != null) {
            throw new RuntimeException("User with current username is already exists");
        }

        PasswordEncoder passwordEncoder = new PasswordEncoder();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userService.create(user);
    }

}
