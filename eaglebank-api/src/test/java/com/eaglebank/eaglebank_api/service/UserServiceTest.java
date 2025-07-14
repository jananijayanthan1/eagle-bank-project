package com.eaglebank.eaglebank_api.service;

import com.eaglebank.eaglebank_api.model.User;
import com.eaglebank.eaglebank_api.model.request.CreateUserRequest;
import com.eaglebank.eaglebank_api.model.response.UserResponse;
import com.eaglebank.eaglebank_api.repository.UserRepository;
import com.eaglebank.eaglebank_api.mapper.UserMapper;
import com.eaglebank.eaglebank_api.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private CreateUserRequest request;
    private User user;
    private UserResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encoded-password");
        request = new CreateUserRequest();
        request.setName("Jane Doe");
        request.setEmail("jane.doe@example.com");
        request.setPhoneNumber("+447911123456");
        request.setPassword("MySecurePassword123");
        user = new User();
        user.setId("usr-12345678");
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPasswordHash("encoded-password");
        response = new UserResponse();
        response.setId("usr-12345678");
        response.setName(request.getName());
        response.setPhoneNumber(request.getPhoneNumber());
        response.setEmail(request.getEmail());
    }

    @Test
    void createUser_Success() {
        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(response);
        UserResponse result = userService.createUser(request);
        assertNotNull(result);
        assertEquals("usr-12345678", result.getId());
        assertEquals("Jane Doe", result.getName());
        assertEquals("jane.doe@example.com", result.getEmail());
    }

    @Test
    void fetchUserById_UserFound_ReturnsUserResponse() {
        when(userRepository.findById("usr-12345678")).thenReturn(java.util.Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);
        UserResponse result = userService.fetchUserById("usr-12345678");
        assertNotNull(result);
        assertEquals("usr-12345678", result.getId());
        assertEquals("Jane Doe", result.getName());
        assertEquals("jane.doe@example.com", result.getEmail());
    }

    @Test
    void fetchUserById_UserNotFound_ThrowsNotFoundException() {
        when(userRepository.findById("usr-99999999")).thenReturn(java.util.Optional.empty());
        Exception exception = assertThrows(com.eaglebank.eaglebank_api.exception.NotFoundException.class, () -> {
            userService.fetchUserById("usr-99999999");
        });
        assertEquals("User not found", exception.getMessage());
    }
   
} 