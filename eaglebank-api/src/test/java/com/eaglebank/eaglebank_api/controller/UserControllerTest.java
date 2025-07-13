package com.eaglebank.eaglebank_api.controller;

import com.eaglebank.eaglebank_api.model.Address;
import com.eaglebank.eaglebank_api.model.request.CreateUserRequest;
import com.eaglebank.eaglebank_api.model.response.UserResponse;
import com.eaglebank.eaglebank_api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import java.time.OffsetDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;
    private CreateUserRequest validRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        Address address = new Address();
        address.setLine1("123 Main St");
        address.setPostcode("E1 6AN");
        validRequest = new CreateUserRequest();
        validRequest.setName("Jane Doe");
        validRequest.setAddress(address);
        validRequest.setPhoneNumber("+447911123456");
        validRequest.setEmail("jane.doe@example.com");
        validRequest.setPassword("MySecurePassword123");
        userResponse = new UserResponse();
        userResponse.setId("usr-12345678");
        userResponse.setName(validRequest.getName());
        userResponse.setAddress(address);
        userResponse.setPhoneNumber(validRequest.getPhoneNumber());
        userResponse.setEmail(validRequest.getEmail());
        userResponse.setCreatedTimestamp(OffsetDateTime.now());
        userResponse.setUpdatedTimestamp(OffsetDateTime.now());
    }

    @Test
    void createUser_ValidRequest_ReturnsCreated() throws Exception {
        Mockito.when(userService.createUser(any(CreateUserRequest.class))).thenReturn(userResponse);
        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("usr-12345678"))
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("jane.doe@example.com"));
    }

    @Test
    void createUser_InvalidRequest_ReturnsBadRequest() throws Exception {
        CreateUserRequest invalidRequest = new CreateUserRequest();
        mockMvc.perform(post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
} 