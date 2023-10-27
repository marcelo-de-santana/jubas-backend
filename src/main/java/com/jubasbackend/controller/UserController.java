package com.jubasbackend.controller;

import com.jubasbackend.dto.RequestMinimalUserDTO;
import com.jubasbackend.dto.RequestUserDTO;
import com.jubasbackend.dto.ResponseMinimalUserDTO;
import com.jubasbackend.dto.ResponseUserDTO;
import com.jubasbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<ResponseMinimalUserDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/permission/{id}")
    public ResponseEntity<List<ResponseUserDTO>> findAllByUserPermission(@PathVariable Short id) {
        return ResponseEntity.ok(userService.findAllByUserPermission(id));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseUserDTO> createUser(@RequestBody RequestUserDTO userToCreate) {
        ResponseUserDTO userCreated = userService.create(userToCreate);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(userCreated.id()).toUri();
        return ResponseEntity.created(location).body(userCreated);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseUserDTO> findUserAccount(@RequestBody RequestMinimalUserDTO user) {
        return ResponseEntity.ok(userService.findUserAccount(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> updateUser(@PathVariable UUID id, @RequestBody RequestUserDTO user) {
        return ResponseEntity.ok(userService.updateUser(id,user));
    }
}