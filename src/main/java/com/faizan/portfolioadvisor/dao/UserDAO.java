// src/main/java/com/faizan/portfolioadvisor/dao/UserDAO.java
package com.faizan.portfolioadvisor.dao;

import com.faizan.portfolioadvisor.model.User;
import com.faizan.portfolioadvisor.util.DatabaseConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /**
     * Adds a new user to the database.
     * The user object's ID will be updated with the generated ID from the DB.
     * @param user The User object to add.
     */
    public void addUser(User user) {
        String sql = "INSERT INTO Users (name, email, password_hash, phone_number, address, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPasswordHash());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getAddress());
            pstmt.setTimestamp(6, Timestamp.valueOf(user.getCreatedAt())); // Convert LocalDateTime to Timestamp

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getInt(1)); // Set the auto-generated ID back to the user object
                    }
                }
            }
            System.out.println("User added successfully: " + user.getEmail() + " with ID: " + user.getUserId());
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            e.printStackTrace();
            // In Phase 2, we'll throw a custom DataAccessException here.
        }
    }

    /**
     * Retrieves a user by their ID from the database.
     * @param userId The ID of the user to retrieve.
     * @return The User object if found, null otherwise.
     */
    public User getUserById(int userId) {
        String sql = "SELECT user_id, name, email, password_hash, phone_number, address, created_at FROM Users WHERE user_id = ?";
        User user = null;
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
                            rs.getString("phone_number"),
                            rs.getString("address"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by ID: " + e.getMessage());
            e.printStackTrace();
            // In Phase 2, we'll throw a custom DataAccessException here.
        }
        return user;
    }

    /**
     * Retrieves a user by their email from the database.
     * @param email The email of the user to retrieve.
     * @return The User object if found, null otherwise.
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT user_id, name, email, password_hash, phone_number, address, created_at FROM Users WHERE email = ?";
        User user = null;
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
                            rs.getString("phone_number"),
                            rs.getString("address"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by email: " + e.getMessage());
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Retrieves all users from the database.
     * @return A List of all User objects.
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, name, email, password_hash, phone_number, address, created_at FROM Users";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Updates an existing user in the database.
     * @param user The User object with updated information. Must have a valid userId.
     * @return true if the user was updated, false otherwise.
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE Users SET name = ?, email = ?, password_hash = ?, phone_number = ?, address = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPasswordHash());
            pstmt.setString(4, user.getPhoneNumber());
            pstmt.setString(5, user.getAddress());
            pstmt.setInt(6, user.getUserId()); // WHERE clause based on ID

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User updated successfully: " + user.getEmail() + " (ID: " + user.getUserId() + ")");
                return true;
            } else {
                System.out.println("No user found with ID: " + user.getUserId() + " for update.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a user from the database by their ID.
     * @param userId The ID of the user to delete.
     * @return true if the user was deleted, false otherwise.
     */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM Users WHERE user_id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User deleted successfully with ID: " + userId);
                return true;
            } else {
                System.out.println("No user found with ID: " + userId + " for deletion.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}