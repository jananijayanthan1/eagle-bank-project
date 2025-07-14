package com.eaglebank.eaglebank_api.repository;

import com.eaglebank.eaglebank_api.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId("usr-12345678");
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        user.setPhoneNumber("+447911123456");
        user.setPasswordHash("MySecurePassword123");
        user.setAddress("{\"line1\":\"123 Main St\",\"line2\":null,\"line3\":null,\"town\":null,\"county\":null,\"postcode\":\"E1 6AN\"}");
        user.setCreatedTimestamp(OffsetDateTime.now());
        user.setUpdatedTimestamp(OffsetDateTime.now());
    }

    @Test
    void saveAndFindById() {
        userRepository.save(user);
        User found = userRepository.findById("usr-12345678").orElse(null);
        assertNotNull(found);
        assertEquals("Jane Doe", found.getName());
        assertEquals("jane.doe@example.com", found.getEmail());
    }

    @Test
    void findByEmail_ReturnsUser() {
        userRepository.save(user);
        User found = userRepository.findByEmail("jane.doe@example.com");
        assertNotNull(found);
        assertEquals("usr-12345678", found.getId());
        assertEquals("Jane Doe", found.getName());
    }

    @Test
    void findById_NotFound_ReturnsEmpty() {
        User found = userRepository.findById("usr-nonexistent").orElse(null);
        assertNull(found);
    }
} 