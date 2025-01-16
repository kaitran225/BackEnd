package com.heathly.BackEnd.controller;

import com.heathly.BackEnd.Service.UserService;
import com.heathly.BackEnd.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("save")
    public String saveUser(@RequestBody UserDTO userDTO) {
        String id = userService.addUser(userDTO);
        return id;
    }
}

