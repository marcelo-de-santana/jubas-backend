package com.jubasbackend.controller;

import com.jubasbackend.dto.UserDTO;
import com.jubasbackend.entity.User;
import com.jubasbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User userToCreate){
        User userCreated = userService.create(userToCreate);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(userCreated.getId()).toUri();
        return ResponseEntity.created(location).body(userCreated);
    }

    @PostMapping("login")
    public ResponseEntity<Object> findUserAccount(@RequestBody User user) {
        UserDTO userAccount = userService.findUserAccount(user);
        if (userAccount != null) {
            return ResponseEntity.ok(userAccount);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário e/ou Senha incorreto(s)");
    }
}
