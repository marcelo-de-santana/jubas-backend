package com.jubasbackend.controller;

import com.jubasbackend.dto.users.MinimalUserDTO;
import com.jubasbackend.entity.User;
import com.jubasbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<MinimalUserDTO> createUser(@RequestBody User userToCreate) {
        MinimalUserDTO userCreated = userService.create(userToCreate);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(userCreated.id()).toUri();
        return ResponseEntity.created(location).body(userCreated);
    }

    @PostMapping("/login")
    public ResponseEntity<MinimalUserDTO> findUserAccount(@RequestBody User user) {
        return ResponseEntity.ok(userService.findUserAccount(user));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MinimalUserDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<MinimalUserDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/all-users/permission/{id}")
    public ResponseEntity<List<MinimalUserDTO>> findAllByUserPermission(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUsersByUserPermission_Id(id));
    }
}