// src/main/java/com/faizan/portfolioadvisor/dao/FinancialProductDAO.java
package com.faizan.portfolioadvisor.dao;

import com.faizan.portfolioadvisor.model.FinancialProduct;
import com.faizan.portfolioadvisor.util.DatabaseConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal; // Import BigDecimal

public class FinancialProductDAO {

    /**
     * Adds a new financial product to the database.
     * The product object's ID will be updated with the generated ID from the DB.
     * @param product The FinancialProduct object to add.
     */
    public void addFinancialProduct(FinancialProduct product) {
        String sql = "INSERT INTO FinancialProducts (name, type, description, risk_level, expected_return_rate, minimum_investment, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getType());
            pstmt.setString(3, product.getDescription());
            pstmt.setString(4, product.getRiskLevel());
            pstmt.setBigDecimal(5, product.getExpectedReturnRate()); // Use setBigDecimal
            pstmt.setBigDecimal(6, product.getMinimumInvestment()); // Use setBigDecimal
            pstmt.setTimestamp(7, Timestamp.valueOf(product.getCreatedAt()));

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setProductId(generatedKeys.getInt(1));
                    }
                }
            }
            System.out.println("Financial Product added successfully: " + product.getName() + " (ID: " + product.getProductId() + ")");
        } catch (SQLException e) {
            System.err.println("Error adding financial product: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Retrieves a financial product by its ID from the database.
     * @param productId The ID of the product to retrieve.
     * @return The FinancialProduct object if found, null otherwise.
     */
    public FinancialProduct getFinancialProductById(int productId) {
        String sql = "SELECT product_id, name, type, description, risk_level, expected_return_rate, minimum_investment, created_at FROM FinancialProducts WHERE product_id = ?";
        FinancialProduct product = null;
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    product = new FinancialProduct(
                            rs.getInt("product_id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getString("description"),
                            rs.getString("risk_level"),
                            rs.getBigDecimal("expected_return_rate"), // Use getBigDecimal
                            rs.getBigDecimal("minimum_investment"),   // Use getBigDecimal
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving financial product by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return product;
    }

    /**
     * Retrieves all financial products from the database.
     * @return A List of all FinancialProduct objects.
     */
    public List<FinancialProduct> getAllFinancialProducts() {
        List<FinancialProduct> products = new ArrayList<>();
        String sql = "SELECT product_id, name, type, description, risk_level, expected_return_rate, minimum_investment, created_at FROM FinancialProducts";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(new FinancialProduct(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("description"),
                        rs.getString("risk_level"),
                        rs.getBigDecimal("expected_return_rate"),
                        rs.getBigDecimal("minimum_investment"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all financial products: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Updates an existing financial product in the database.
     * @param product The FinancialProduct object with updated information. Must have a valid productId.
     * @return true if the product was updated, false otherwise.
     */
    public boolean updateFinancialProduct(FinancialProduct product) {
        String sql = "UPDATE FinancialProducts SET name = ?, type = ?, description = ?, risk_level = ?, expected_return_rate = ?, minimum_investment = ? WHERE product_id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getType());
            pstmt.setString(3, product.getDescription());
            pstmt.setString(4, product.getRiskLevel());
            pstmt.setBigDecimal(5, product.getExpectedReturnRate());
            pstmt.setBigDecimal(6, product.getMinimumInvestment());
            pstmt.setInt(7, product.getProductId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Financial Product updated successfully: " + product.getName() + " (ID: " + product.getProductId() + ")");
                return true;
            } else {
                System.out.println("No financial product found with ID: " + product.getProductId() + " for update.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error updating financial product: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a financial product from the database by its ID.
     * @param productId The ID of the product to delete.
     * @return true if the product was deleted, false otherwise.
     */
    public boolean deleteFinancialProduct(int productId) {
        String sql = "DELETE FROM FinancialProducts WHERE product_id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, productId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Financial Product deleted successfully with ID: " + productId);
                return true;
            } else {
                System.out.println("No financial product found with ID: " + productId + " for deletion.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting financial product: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}