package com.eaglebank.eaglebank_api.repository;

import com.eaglebank.eaglebank_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // Custom queries if needed
    User findByEmail(String email);
} 