package com.jubasbackend.service;

import com.jubasbackend.dto.UserDTO;
import com.jubasbackend.entity.User;
import com.jubasbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User create(User userToCreate){
        if(userRepository.existsByEmail(userToCreate.getEmail())){
            throw new IllegalArgumentException("Usuário já existe");
        } else {
            return userRepository.save(userToCreate);
        }
    }

    public UserDTO findUserAccount(User user) {
        User userData = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword());
        return new UserDTO(userData);
    }
}
