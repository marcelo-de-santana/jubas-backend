package com.jubasbackend.service;

import com.jubasbackend.dto.UserDTO;
import com.jubasbackend.entity.User;
import com.jubasbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO create(User userToCreate) {
        if (userRepository.existsByEmail(userToCreate.getEmail())) {
            throw new IllegalArgumentException("User already exists.");
        }
        return new UserDTO(userRepository.save(userToCreate));
    }

    public UserDTO findUserAccount(User user) {
        return new UserDTO(userRepository
                .findByEmailAndPassword(user.getEmail(), user.getPassword())
                .orElseThrow(
                        () -> new IllegalArgumentException("Incorrect Email or Password.")));
    }

    public UserDTO findById(UUID id) {
        return new UserDTO(userRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("User not exists.")));
    }
}