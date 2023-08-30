package com.jubasbackend.service;

import com.jubasbackend.dto.users.MinimalUserDTO;
import com.jubasbackend.entity.User;
import com.jubasbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public MinimalUserDTO create(User userToCreate) {
        if (userRepository.existsByEmail(userToCreate.getEmail())) {
            throw new IllegalArgumentException("User already exists.");
        }
        return new MinimalUserDTO(userRepository.save(userToCreate));
    }

    public MinimalUserDTO findUserAccount(User user) {
        return new MinimalUserDTO(userRepository
                .findByEmailAndPassword(user.getEmail(), user.getPassword())
                .orElseThrow(
                        () -> new IllegalArgumentException("Incorrect Email or Password.")));
    }

    public MinimalUserDTO findById(UUID id) {
        return new MinimalUserDTO(userRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchElementException("User not exists.")));
    }

    public List<MinimalUserDTO> findAll() {
        return (userRepository.findAll()
                .stream().map(MinimalUserDTO::new).toList());
    }

    public List<MinimalUserDTO> findUsersByUserPermission_Id(Long id) {
        return (userRepository.findUsersByUserPermission_Id(id)
                .stream().map(MinimalUserDTO::new).toList());
    }
}