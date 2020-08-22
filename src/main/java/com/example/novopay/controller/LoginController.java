package com.example.novopay.controller;

import com.example.novopay.model.User;
import com.example.novopay.service.UserService;
import com.example.novopay.utils.PayResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/login")
public class LoginController {

    private final UserService userService;

    @PostMapping("/signup")
    public PayResponse signUp(@Valid @RequestBody User user) {
        return userService.signUp(user);
    }

    @PostMapping("/signin")
    public PayResponse signIn(@Valid @RequestBody User user) {
        return userService.signIn(user);
    }
}
