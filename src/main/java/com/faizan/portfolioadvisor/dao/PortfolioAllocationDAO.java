// src/main/java/com/faizan/portfolioadvisor/dao/PortfolioAllocationDAO.java
package com.faizan.portfolioadvisor.dao;

import com.faizan.portfolioadvisor.model.PortfolioAllocation;
import com.faizan.portfolioadvisor.util.DatabaseConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal; // Import BigDecimal

public class PortfolioAllocationDAO {

    /**
     * Adds a new portfolio allocation to the database.
     * The allocation object's ID will be updated with the generated ID from the DB.
     * @param allocation The PortfolioAllocation object to add.
     */
    public void addPortfolioAllocation(PortfolioAllocation allocation) {
        String sql = "INSERT INTO PortfolioAllocations (user_id, recommendation_date, equity_pct, debt_pct, alternative_pct, other_details) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, allocation.getUserId());
            pstmt.setTimestamp(2, Timestamp.valueOf(allocation.getRecommendationDate()));
            pstmt.setBigDecimal(3, allocation.getEquityPct());
            pstmt.setBigDecimal(4, allocation.getDebtPct());
            pstmt.setBigDecimal(5, allocation.getAlternativePct());
            pstmt.setString(6, allocation.getOtherDetails());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        allocation.setAllocationId(generatedKeys.getInt(1));
                    }
                }
            }
            System.out.println("Portfolio Allocation added successfully for User ID: " + allocation.getUserId() + " (ID: " + allocation.getAllocationId() + ")");
        } catch (SQLException e) {
            System.err.println("Error adding portfolio allocation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all portfolio allocations for a specific user.
     * @param userId The ID of the user.
     * @return A List of all PortfolioAllocation objects for the user.
     */
    public List<PortfolioAllocation> getPortfolioAllocationsByUserId(int userId) {
        List<PortfolioAllocation> allocations = new ArrayList<>();
        String sql = "SELECT allocation_id, user_id, recommendation_date, equity_pct, debt_pct, alternative_pct, other_details FROM PortfolioAllocations WHERE user_id = ? ORDER BY recommendation_date DESC";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    allocations.add(new PortfolioAllocation(
                            rs.getInt("allocation_id"),
                            rs.getInt("user_id"),
                            rs.getTimestamp("recommendation_date").toLocalDateTime(),
                            rs.getBigDecimal("equity_pct"),
                            rs.getBigDecimal("debt_pct"),
                            rs.getBigDecimal("alternative_pct"),
                            rs.getString("other_details")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving portfolio allocations for User ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return allocations;
    }

    /**
     * Retrieves a specific portfolio allocation by its ID.
     * @param allocationId The ID of the allocation to retrieve.
     * @return The PortfolioAllocation object if found, null otherwise.
     */
    public PortfolioAllocation getPortfolioAllocationById(int allocationId) {
        String sql = "SELECT allocation_id, user_id, recommendation_date, equity_pct, debt_pct, alternative_pct, other_details FROM PortfolioAllocations WHERE allocation_id = ?";
        PortfolioAllocation allocation = null;
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, allocationId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    allocation = new PortfolioAllocation(
                            rs.getInt("allocation_id"),
                            rs.getInt("user_id"),
                            rs.getTimestamp("recommendation_date").toLocalDateTime(),
                            rs.getBigDecimal("equity_pct"),
                            rs.getBigDecimal("debt_pct"),
                            rs.getBigDecimal("alternative_pct"),
                            rs.getString("other_details")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving portfolio allocation by ID " + allocationId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return allocation;
    }

    /**
     * Updates an existing portfolio allocation in the database.
     * @param allocation The PortfolioAllocation object with updated information. Must have a valid allocationId.
     * @return true if the allocation was updated, false otherwise.
     */
    public boolean updatePortfolioAllocation(PortfolioAllocation allocation) {
        String sql = "UPDATE PortfolioAllocations SET user_id = ?, recommendation_date = ?, equity_pct = ?, debt_pct = ?, alternative_pct = ?, other_details = ? WHERE allocation_id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, allocation.getUserId());
            pstmt.setTimestamp(2, Timestamp.valueOf(allocation.getRecommendationDate()));
            pstmt.setBigDecimal(3, allocation.getEquityPct());
            pstmt.setBigDecimal(4, allocation.getDebtPct());
            pstmt.setBigDecimal(5, allocation.getAlternativePct());
            pstmt.setString(6, allocation.getOtherDetails());
            pstmt.setInt(7, allocation.getAllocationId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Portfolio Allocation updated successfully for ID: " + allocation.getAllocationId());
                return true;
            } else {
                System.out.println("No portfolio allocation found with ID: " + allocation.getAllocationId() + " for update.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error updating portfolio allocation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a portfolio allocation from the database by its ID.
     * @param allocationId The ID of the allocation to delete.
     * @return true if the allocation was deleted, false otherwise.
     */
    public boolean deletePortfolioAllocation(int allocationId) {
        String sql = "DELETE FROM PortfolioAllocations WHERE allocation_id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, allocationId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Portfolio Allocation deleted successfully with ID: " + allocationId);
                return true;
            } else {
                System.out.println("No portfolio allocation found with ID: " + allocationId + " for deletion.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting portfolio allocation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}