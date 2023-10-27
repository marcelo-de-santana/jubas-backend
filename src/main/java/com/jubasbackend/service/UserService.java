package com.jubasbackend.service;

import com.jubasbackend.domain.entity.User;
import com.jubasbackend.domain.entity.UserPermission;
import com.jubasbackend.domain.repository.UserRepository;
import com.jubasbackend.dto.RequestMinimalUserDTO;
import com.jubasbackend.dto.RequestUserDTO;
import com.jubasbackend.dto.ResponseMinimalUserDTO;
import com.jubasbackend.dto.ResponseUserDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    protected User findUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User doesn't exists."));
    }

    public ResponseUserDTO create(RequestUserDTO userToCreate) {
        if (userRepository.existsByEmail(userToCreate.email())) {
            throw new IllegalArgumentException("User already exists.");
        }
        User userToSave = new User(userToCreate);
        return new ResponseUserDTO(userRepository.save(userToSave));
    }

    public ResponseUserDTO findUserAccount(RequestMinimalUserDTO user) {
        return new ResponseUserDTO(userRepository.findByEmailAndPassword(user.email(), user.password())
                .orElseThrow(
                        () -> new IllegalArgumentException("Incorrect Email or Password.")));
    }

    public ResponseUserDTO findById(UUID id) {
        return new ResponseUserDTO(findUserById(id));
    }

    public List<ResponseMinimalUserDTO> findAll() {
        return userRepository.findAll().stream().map(ResponseMinimalUserDTO::new).toList();
    }

    public List<ResponseUserDTO> findAllByUserPermission(Short id) {
        return (userRepository.findUsersByUserPermission_Id(id).stream().map(ResponseUserDTO::new).toList());
    }

    public ResponseUserDTO updateUser(UUID id, RequestUserDTO user) {
        User userToUpdate = findUserById(id);
        if (user.password() != null && user.password().length() >= 8) {
            userToUpdate.setPassword(user.password());
        }
        userToUpdate.setEmail(user.email());
        userToUpdate.setUserPermission(new UserPermission(user.userPermissionId()));
        return new ResponseUserDTO(userRepository.save(userToUpdate));
    }
}