// src/main/java/com/faizan/portfolioadvisor/model/FinancialProduct.java
package com.faizan.portfolioadvisor.model;

import java.math.BigDecimal; // For DECIMAL data type from SQL
import java.time.LocalDateTime;
import java.util.Objects;

public class FinancialProduct {
    private int productId;
    private String name;
    private String type; // e.g., 'Stock', 'Bond', 'Mutual Fund', 'ETF'
    private String description;
    private String riskLevel; // e.g., 'Low', 'Medium', 'High'
    private BigDecimal expectedReturnRate; // Corresponds to DECIMAL(5,2)
    private BigDecimal minimumInvestment; // Corresponds to DECIMAL(15,2)
    private LocalDateTime createdAt;

    // Default constructor
    public FinancialProduct() {
    }

    // Constructor for creating new products (without ID and creation timestamp initially)
    public FinancialProduct(String name, String type, String description, String riskLevel, BigDecimal expectedReturnRate, BigDecimal minimumInvestment) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.riskLevel = riskLevel;
        this.expectedReturnRate = expectedReturnRate;
        this.minimumInvestment = minimumInvestment;
        this.createdAt = LocalDateTime.now(); // Set current time
    }

    // Constructor for retrieving existing products from DB (with ID and createdAt)
    public FinancialProduct(int productId, String name, String type, String description, String riskLevel, BigDecimal expectedReturnRate, BigDecimal minimumInvestment, LocalDateTime createdAt) {
        this.productId = productId;
        this.name = name;
        this.type = type;
        this.description = description;
        this.riskLevel = riskLevel;
        this.expectedReturnRate = expectedReturnRate;
        this.minimumInvestment = minimumInvestment;
        this.createdAt = createdAt;
    }

    // --- Getters ---
    public int getProductId() { return productId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public String getRiskLevel() { return riskLevel; }
    public BigDecimal getExpectedReturnRate() { return expectedReturnRate; }
    public BigDecimal getMinimumInvestment() { return minimumInvestment; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // --- Setters ---
    public void setProductId(int productId) { this.productId = productId; }
    public void setName(String name) { this.name = name; }
    public void getType(String type) { this.type = type; } // Typo: Should be setType
    public void setType(String type) { this.type = type; }
    public void setDescription(String description) { this.description = description; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public void setExpectedReturnRate(BigDecimal expectedReturnRate) { this.expectedReturnRate = expectedReturnRate; }
    public void setMinimumInvestment(BigDecimal minimumInvestment) { this.minimumInvestment = minimumInvestment; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "FinancialProduct{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", riskLevel='" + riskLevel + '\'' +
                ", expectedReturnRate=" + expectedReturnRate +
                ", minimumInvestment=" + minimumInvestment +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinancialProduct that = (FinancialProduct) o;
        return productId == that.productId && name.equals(that.name) && type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, name, type);
    }
}