package com.jubasbackend.service;

import com.jubasbackend.dto.UserDTO;
import com.jubasbackend.entity.User;
import com.jubasbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User create(User userToCreate) {
        if (userRepository.existsByEmail(userToCreate.getEmail())) {
            throw new IllegalArgumentException("Usuário já existe");
        }
        return userRepository.save(userToCreate);
    }

    public UserDTO findUserAccount(User user) {
        User userData = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());

        if (userData == null) {
            return null;
        }

        return new UserDTO(userData);
    }
}
