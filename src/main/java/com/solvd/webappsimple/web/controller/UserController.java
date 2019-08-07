package com.solvd.webappsimple.web.controller;

import com.solvd.webappsimple.domain.User;
import com.solvd.webappsimple.service.UserService;
import com.solvd.webappsimple.web.annotation.GetMapping;
import com.solvd.webappsimple.web.annotation.PathVariable;
import com.solvd.webappsimple.web.annotation.PostMapping;
import com.solvd.webappsimple.web.annotation.RequestBody;
import com.solvd.webappsimple.web.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/users")
public class UserController extends BaseController {

    @PostMapping
    public User createUser(@RequestBody User user, HttpServletRequest req, HttpServletResponse resp) {
        UserService userService = getService(UserService.class);
        return userService.create(user);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable("id") Long id) {
        UserService userService = getService(UserService.class);
        return userService.getById(id);
    }

    @GetMapping
    public List<User> getAll() {
        UserService userService = getService(UserService.class);
        return userService.getAll();
    }

}
