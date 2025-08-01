// src/main/java/com/faizan/portfolioadvisor/dao/UserRiskProfileDAO.java
package com.faizan.portfolioadvisor.dao;

import com.faizan.portfolioadvisor.model.UserRiskProfile;
import com.faizan.portfolioadvisor.util.DatabaseConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal; // Import BigDecimal

public class UserRiskProfileDAO {

    /**
     * Adds a new user risk profile to the database.
     * The riskProfile object's ID will be updated with the generated ID from the DB.
     * @param riskProfile The UserRiskProfile object to add.
     */
    public void addUserRiskProfile(UserRiskProfile riskProfile) {
        String sql = "INSERT INTO UserRiskProfiles (user_id, predicted_risk_category, prediction_date, confidence_score, age, income_lakhs, investment_experience_years, financial_goal) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, riskProfile.getUserId());
            pstmt.setString(2, riskProfile.getPredictedRiskCategory());
            pstmt.setTimestamp(3, Timestamp.valueOf(riskProfile.getPredictionDate()));
            pstmt.setBigDecimal(4, riskProfile.getConfidenceScore());
            // Use setNull if Integer/BigDecimal wrapper types are null
            if (riskProfile.getAge() != null) {
                pstmt.setInt(5, riskProfile.getAge());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }
            pstmt.setBigDecimal(6, riskProfile.getIncomeLakhs());
            if (riskProfile.getInvestmentExperienceYears() != null) {
                pstmt.setInt(7, riskProfile.getInvestmentExperienceYears());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }
            pstmt.setString(8, riskProfile.getFinancialGoal());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        riskProfile.setRiskProfileId(generatedKeys.getInt(1));
                    }
                }
            }
            System.out.println("User Risk Profile added successfully for User ID: " + riskProfile.getUserId() + " (ID: " + riskProfile.getRiskProfileId() + ")");
        } catch (SQLException e) {
            System.err.println("Error adding user risk profile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the latest risk profile for a specific user from the database.
     * Assumes "latest" means the one with the most recent prediction_date.
     * @param userId The ID of the user.
     * @return The latest UserRiskProfile object if found, null otherwise.
     */
    public UserRiskProfile getLatestUserRiskProfile(int userId) {
        String sql = "SELECT risk_profile_id, user_id, predicted_risk_category, prediction_date, confidence_score, age, income_lakhs, investment_experience_years, financial_goal " +
                "FROM UserRiskProfiles WHERE user_id = ? ORDER BY prediction_date DESC LIMIT 1";
        UserRiskProfile riskProfile = null;
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    riskProfile = new UserRiskProfile(
                            rs.getInt("risk_profile_id"),
                            rs.getInt("user_id"),
                            rs.getString("predicted_risk_category"),
                            rs.getTimestamp("prediction_date").toLocalDateTime(),
                            rs.getBigDecimal("confidence_score"),
                            (Integer) rs.getObject("age"), // Use getObject to handle potential nulls for Integer
                            rs.getBigDecimal("income_lakhs"),
                            (Integer) rs.getObject("investment_experience_years"),
                            rs.getString("financial_goal")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving latest user risk profile for User ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return riskProfile;
    }

    /**
     * Retrieves all risk profiles for a specific user.
     * @param userId The ID of the user.
     * @return A List of all UserRiskProfile objects for the user.
     */
    public List<UserRiskProfile> getUserRiskProfilesByUserId(int userId) {
        List<UserRiskProfile> profiles = new ArrayList<>();
        String sql = "SELECT risk_profile_id, user_id, predicted_risk_category, prediction_date, confidence_score, age, income_lakhs, investment_experience_years, financial_goal " +
                "FROM UserRiskProfiles WHERE user_id = ? ORDER BY prediction_date DESC";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    profiles.add(new UserRiskProfile(
                            rs.getInt("risk_profile_id"),
                            rs.getInt("user_id"),
                            rs.getString("predicted_risk_category"),
                            rs.getTimestamp("prediction_date").toLocalDateTime(),
                            rs.getBigDecimal("confidence_score"),
                            (Integer) rs.getObject("age"),
                            rs.getBigDecimal("income_lakhs"),
                            (Integer) rs.getObject("investment_experience_years"),
                            rs.getString("financial_goal")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user risk profiles for User ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return profiles;
    }

    /**
     * Updates an existing user risk profile in the database.
     * Typically, for risk profiles, new ones are added rather than updating old ones,
     * but this method is included for full CRUD.
     * @param riskProfile The UserRiskProfile object with updated information. Must have a valid riskProfileId.
     * @return true if the profile was updated, false otherwise.
     */
    public boolean updateUserRiskProfile(UserRiskProfile riskProfile) {
        String sql = "UPDATE UserRiskProfiles SET user_id = ?, predicted_risk_category = ?, prediction_date = ?, confidence_score = ?, age = ?, income_lakhs = ?, investment_experience_years = ?, financial_goal = ? WHERE risk_profile_id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, riskProfile.getUserId());
            pstmt.setString(2, riskProfile.getPredictedRiskCategory());
            pstmt.setTimestamp(3, Timestamp.valueOf(riskProfile.getPredictionDate()));
            pstmt.setBigDecimal(4, riskProfile.getConfidenceScore());
            if (riskProfile.getAge() != null) {
                pstmt.setInt(5, riskProfile.getAge());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }
            pstmt.setBigDecimal(6, riskProfile.getIncomeLakhs());
            if (riskProfile.getInvestmentExperienceYears() != null) {
                pstmt.setInt(7, riskProfile.getInvestmentExperienceYears());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }
            pstmt.setString(8, riskProfile.getFinancialGoal());
            pstmt.setInt(9, riskProfile.getRiskProfileId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User Risk Profile updated successfully for ID: " + riskProfile.getRiskProfileId());
                return true;
            } else {
                System.out.println("No user risk profile found with ID: " + riskProfile.getRiskProfileId() + " for update.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error updating user risk profile: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a user risk profile from the database by its ID.
     * @param riskProfileId The ID of the risk profile to delete.
     * @return true if the profile was deleted, false otherwise.
     */
    public boolean deleteUserRiskProfile(int riskProfileId) {
        String sql = "DELETE FROM UserRiskProfiles WHERE risk_profile_id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, riskProfileId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("User Risk Profile deleted successfully with ID: " + riskProfileId);
                return true;
            } else {
                System.out.println("No user risk profile found with ID: " + riskProfileId + " for deletion.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting user risk profile: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}