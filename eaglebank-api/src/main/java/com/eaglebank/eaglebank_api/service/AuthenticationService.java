package com.eaglebank.eaglebank_api.service;

import com.eaglebank.eaglebank_api.exception.AuthenticationException;
import com.eaglebank.eaglebank_api.model.User;
import com.eaglebank.eaglebank_api.model.request.AuthRequest;
import com.eaglebank.eaglebank_api.model.response.AuthResponse;
import com.eaglebank.eaglebank_api.repository.UserRepository;
import com.eaglebank.eaglebank_api.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    public AuthenticationService(UserRepository userRepository, 
                               PasswordEncoder passwordEncoder, 
                               JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }
    
    public AuthResponse authenticate(AuthRequest request) {
        // Find user by email
        System.out.println("Finding user by email: " + request.getEmail());
        User user = userRepository.findByEmail(request.getEmail());
        
        if (user == null) {
            System.out.println("User not found with email: " + request.getEmail());
            throw new AuthenticationException("User not found with email: " + request.getEmail());
        }
        
        // Compare the hashed password from database with the encoded password from request
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            System.out.println("Invalid password");
            throw new AuthenticationException("Invalid password");
        }
        
        // Generate JWT token with user ID and email
        String token = jwtUtil.generateToken(user.getId(), user.getEmail());
        System.out.println("Token generated: " + token);
        return new AuthResponse(token);
    }
}
