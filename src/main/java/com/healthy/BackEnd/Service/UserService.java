package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean isUserEmpty() {
        return userRepository.findAll().isEmpty();
    }
}