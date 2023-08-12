package com.jubasbackend.controller;

import com.jubasbackend.entity.User;
import com.jubasbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Object> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }
}
