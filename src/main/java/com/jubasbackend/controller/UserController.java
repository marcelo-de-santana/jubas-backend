package com.jubasbackend.controller;

import com.jubasbackend.entity.User;
import com.jubasbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ResponseEntity<Object> findUserAccount(@RequestBody User user) {
        var userAccount = userService.findUserAccount(user);
        if (userAccount != null) {
            return ResponseEntity.ok(userAccount);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário e/ou Senha incorreto(s)");
    }
}
