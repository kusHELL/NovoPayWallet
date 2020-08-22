package com.example.novopay.service;

import com.example.novopay.model.User;
import com.example.novopay.utils.PayResponse;

import java.util.Optional;

public interface UserService {
    PayResponse signUp(User user);

    PayResponse signIn(User user);

    Optional<User> findUser(String emailId);
}
