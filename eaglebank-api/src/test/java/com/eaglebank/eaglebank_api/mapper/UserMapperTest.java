package com.eaglebank.eaglebank_api.mapper;

import com.eaglebank.eaglebank_api.model.Address;
import com.eaglebank.eaglebank_api.model.User;
import com.eaglebank.eaglebank_api.model.request.CreateUserRequest;
import com.eaglebank.eaglebank_api.model.response.UserResponse;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {
    private final UserMapper userMapper = new UserMapper();

    @Test
    void toEntity_ValidRequest_MapsAllFields() {
        CreateUserRequest req = new CreateUserRequest();
        Address addr = new Address();
        addr.setLine1("123 Main St");
        addr.setLine2("Apt 4B");
        addr.setLine3("Building A");
        addr.setTown("London");
        addr.setCounty("Greater London");
        addr.setPostcode("E1 6AN");
        req.setName("Jane Doe");
        req.setAddress(addr);
        req.setPhoneNumber("+447911123456");
        req.setEmail("jane.doe@example.com");
        req.setPassword("MySecurePassword123");
        User user = userMapper.toEntity(req);
        assertNotNull(user.getId());
        assertEquals("Jane Doe", user.getName());
        assertEquals("jane.doe@example.com", user.getEmail());
        assertEquals("+447911123456", user.getPhoneNumber());
        assertEquals("MySecurePassword123", user.getPasswordHash());
        assertNotNull(user.getAddress());
        assertNotNull(user.getCreatedTimestamp());
        assertNotNull(user.getUpdatedTimestamp());
    }

    @Test
    void toResponse_ValidUser_MapsAllFields() {
        User user = new User();
        user.setId("usr-12345678");
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        user.setPhoneNumber("+447911123456");
        user.setPasswordHash("MySecurePassword123");
        Address addr = new Address();
        addr.setLine1("123 Main St");
        addr.setLine2("Apt 4B");
        addr.setLine3("Building A");
        addr.setTown("London");
        addr.setCounty("Greater London");
        addr.setPostcode("E1 6AN");
        try {
            user.setAddress(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(addr));
        } catch (Exception e) {
            fail("Address serialization failed");
        }
        user.setCreatedTimestamp(OffsetDateTime.now());
        user.setUpdatedTimestamp(OffsetDateTime.now());
        UserResponse resp = userMapper.toResponse(user);
        assertEquals("usr-12345678", resp.getId());
        assertEquals("Jane Doe", resp.getName());
        assertEquals("jane.doe@example.com", resp.getEmail());
        assertEquals("+447911123456", resp.getPhoneNumber());
        assertNotNull(resp.getAddress());
        assertEquals("123 Main St", resp.getAddress().getLine1());
        assertNotNull(resp.getCreatedTimestamp());
        assertNotNull(resp.getUpdatedTimestamp());
    }

    @Test
    void toResponse_InvalidAddressJson_SetsAddressNull() {
        User user = new User();
        user.setId("usr-12345678");
        user.setName("Jane Doe");
        user.setEmail("jane.doe@example.com");
        user.setPhoneNumber("+447911123456");
        user.setPasswordHash("MySecurePassword123");
        user.setAddress("not-a-json");
        user.setCreatedTimestamp(OffsetDateTime.now());
        user.setUpdatedTimestamp(OffsetDateTime.now());
        UserResponse resp = userMapper.toResponse(user);
        assertNull(resp.getAddress());
    }
} 