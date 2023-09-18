package com.jubasbackend.service;

import com.jubasbackend.dto.user.MinimalUserDTO;
import com.jubasbackend.dto.user.UserDTO;
import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
                        () -> new NoSuchElementException("User doesn't exists.")));
    }

    public List<MinimalUserDTO> findAll() {
        return userRepository.findAll().stream().map(MinimalUserDTO::new).toList();
    }

    public List<UserDTO> findAllByUserPermission(Short id) {
        return (userRepository.findUsersByUserPermission_Id(id)
                .stream().map(UserDTO::new).toList());
    }

    public UserDTO updateUser(User user) {
        User optionalUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new NoSuchElementException("User doesn't exist."));
        optionalUser.setEmail(user.getEmail());
        if (user.getPassword() != null) {
            if (user.getPassword().length() >= 8) {
                optionalUser.setPassword(user.getPassword());
            }
        }
        optionalUser.setUserPermission(user.getUserPermission());
        return new UserDTO(userRepository.save(optionalUser));
    }
}