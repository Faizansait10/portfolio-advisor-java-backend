// src/main/java/com/faizan/portfolioadvisor/service/UserService.java
package com.faizan.portfolioadvisor.service;

import com.faizan.portfolioadvisor.dao.UserDAO;
import com.faizan.portfolioadvisor.exception.AuthenticationException;
import com.faizan.portfolioadvisor.exception.InvalidInputException;
import com.faizan.portfolioadvisor.model.User;

import java.util.List;

public class UserService {
    private final UserDAO userDAO;

    // Constructor-based Dependency Injection
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User registerUser(String name, String email, String passwordHash, String phoneNumber, String address) {
        // Basic validation
        if (email == null || !email.contains("@")) {
            throw new InvalidInputException("Invalid email format.");
        }
        if (passwordHash == null || passwordHash.length() < 6) {
            throw new InvalidInputException("Password must be at least 6 characters.");
        }

        // Check if user already exists
        if (userDAO.getUserByEmail(email) != null) {
            throw new InvalidInputException("User with this email already exists.");
        }

        User newUser = new User(name, email, passwordHash, phoneNumber, address);
        userDAO.addUser(newUser); // This method call handles the DB insertion
        return newUser;
    }

    public User userLogin(String email, String passwordHash) {
        User user = userDAO.getUserByEmail(email);
        if (user == null || !user.getPasswordHash().equals(passwordHash)) {
            throw new AuthenticationException("Invalid email or password.");
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // More methods like updateUser, deleteUser, etc., can be added here
}