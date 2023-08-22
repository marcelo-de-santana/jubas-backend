package com.jubasbackend.controller;

import com.jubasbackend.entity.User;
import com.jubasbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User userToCreate) {
        User userCreated = userService.create(userToCreate);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(userCreated.getId()).toUri();
        return ResponseEntity.created(location).body(userCreated);
    }

    @PostMapping("login")
    public ResponseEntity<Object> findUserAccount(@RequestBody User user) {
        return ResponseEntity.ok(userService.findUserAccount(user));
    }
}
