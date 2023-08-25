package com.jubasbackend.controller;

import com.jubasbackend.dto.UserDTO;
import com.jubasbackend.entity.User;
import com.jubasbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User userToCreate) {
        UserDTO userCreated = userService.create(userToCreate);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(userCreated.id()).toUri();
        return ResponseEntity.created(location).body(userCreated);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> findUserAccount(@RequestBody User user) {
        return ResponseEntity.ok(userService.findUserAccount(user));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }
}