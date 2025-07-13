package com.eaglebank.eaglebank_api.service;

import com.eaglebank.eaglebank_api.model.User;
import com.eaglebank.eaglebank_api.model.request.CreateUserRequest;
import com.eaglebank.eaglebank_api.model.response.UserResponse;
import com.eaglebank.eaglebank_api.repository.UserRepository;
import com.eaglebank.eaglebank_api.mapper.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        System.out.println("Password: " + request.getPassword());
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        System.out.println("Password: " + request.getPassword());
        User user = userMapper.toEntity(request);
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }
} 