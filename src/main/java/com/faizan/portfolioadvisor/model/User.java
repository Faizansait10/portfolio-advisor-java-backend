// src/main/java/com/faizan/portfolioadvisor/model/User.java
package com.faizan.portfolioadvisor.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private int userId;
    private String name;
    private String email;
    private String passwordHash; // Store actual hashed passwords in real apps
    private String phoneNumber;
    private String address;
    private LocalDateTime createdAt;

    // Default constructor
    public User() {
    }

    // Constructor for creating new users (without ID and creation timestamp initially)
    public User(String name, String email, String passwordHash, String phoneNumber, String address) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.createdAt = LocalDateTime.now(); // Set current time on creation, though DB will override default
    }

    // Constructor for retrieving existing users from DB (with ID and createdAt)
    public User(int userId, String name, String email, String passwordHash, String phoneNumber, String address, LocalDateTime createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.createdAt = createdAt;
    }

    // --- Getters ---
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // --- Setters ---
    public void setUserId(int userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setAddress(String address) { this.address = address; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email);
    }
}