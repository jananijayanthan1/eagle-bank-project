package com.eaglebank.eaglebank_api.model;

import java.time.OffsetDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String passwordHash;
    private String phoneNumber;
    private String address; 
    // Store as JSON string or split into fields as needed
    @Column(name = "created_timestamp", nullable = false, updatable = false)
    // @CreationTimestamp
    private OffsetDateTime createdTimestamp;

    @Column(name = "updated_timestamp", nullable = false)
    // @UpdateTimestamp
    private OffsetDateTime updatedTimestamp;
} 