package com.eaglebank.eaglebank_api.mapper;

import com.eaglebank.eaglebank_api.model.User;
import com.eaglebank.eaglebank_api.model.request.CreateUserRequest;
import com.eaglebank.eaglebank_api.model.response.UserResponse;
import com.eaglebank.eaglebank_api.model.Address;

import java.time.OffsetDateTime;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UserMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setId("usr-" + java.util.UUID.randomUUID().toString().replace("-", "").substring(0, 8));
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPasswordHash(request.getPassword());
        try {
            user.setAddress(objectMapper.writeValueAsString(request.getAddress()));
        } catch (JsonProcessingException e) {
            user.setAddress(null);
        }
        
        //hardcoded for now but will be replaced with @CreationTimestamp and @UpdateTimestamp at later stage
        user.setCreatedTimestamp(OffsetDateTime.now());
        user.setUpdatedTimestamp(OffsetDateTime.now());
        return user;
    }

    public UserResponse toResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setCreatedTimestamp(user.getCreatedTimestamp());
        response.setUpdatedTimestamp(user.getUpdatedTimestamp());
        try {
            Address address = objectMapper.readValue(user.getAddress(), Address.class);
            response.setAddress(address);
        } catch (Exception e) {
            response.setAddress(null);
        }
        // Set timestamps as needed
        System.out.println("User created: " + user.getCreatedTimestamp());
        System.out.println("User updated: " + user.getUpdatedTimestamp());
        return response;
    }
} 