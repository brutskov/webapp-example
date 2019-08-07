package com.solvd.webappsimple.web;

import com.solvd.webappsimple.domain.User;
import com.solvd.webappsimple.web.dto.LoginModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AuthController {

    @PostMapping("/login")
    public String login(@RequestBody LoginModel loginModel) {
        return null;
    }

    @PostMapping("/signup")
    public void signup(@RequestBody User user) {

    }

}
