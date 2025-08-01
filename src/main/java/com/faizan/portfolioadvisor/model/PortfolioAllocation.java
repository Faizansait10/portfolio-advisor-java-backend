// src/main/java/com/faizan/portfolioadvisor/model/PortfolioAllocation.java
package com.faizan.portfolioadvisor.model;

import java.math.BigDecimal; // For DECIMAL data type from SQL
import java.time.LocalDateTime;
import java.util.Objects;

public class PortfolioAllocation {
    private int allocationId;
    private int userId;
    private LocalDateTime recommendationDate;
    private BigDecimal equityPct; // Corresponds to DECIMAL(5,2)
    private BigDecimal debtPct;   // Corresponds to DECIMAL(5,2)
    private BigDecimal alternativePct; // Corresponds to DECIMAL(5,2)
    private String otherDetails; // For text recommendations

    // Default constructor
    public PortfolioAllocation() {
    }

    // Constructor for creating new allocations (without ID initially)
    public PortfolioAllocation(int userId, BigDecimal equityPct, BigDecimal debtPct, BigDecimal alternativePct, String otherDetails) {
        this.userId = userId;
        this.recommendationDate = LocalDateTime.now(); // Set current time
        this.equityPct = equityPct;
        this.debtPct = debtPct;
        this.alternativePct = alternativePct;
        this.otherDetails = otherDetails;
    }

    // Constructor for retrieving existing allocations from DB (with ID and recommendationDate)
    public PortfolioAllocation(int allocationId, int userId, LocalDateTime recommendationDate, BigDecimal equityPct, BigDecimal debtPct, BigDecimal alternativePct, String otherDetails) {
        this.allocationId = allocationId;
        this.userId = userId;
        this.recommendationDate = recommendationDate;
        this.equityPct = equityPct;
        this.debtPct = debtPct;
        this.alternativePct = alternativePct;
        this.otherDetails = otherDetails;
    }

    // --- Getters ---
    public int getAllocationId() { return allocationId; }
    public int getUserId() { return userId; }
    public LocalDateTime getRecommendationDate() { return recommendationDate; }
    public BigDecimal getEquityPct() { return equityPct; }
    public BigDecimal getDebtPct() { return debtPct; }
    public BigDecimal getAlternativePct() { return alternativePct; }
    public String getOtherDetails() { return otherDetails; }

    // --- Setters ---
    public void setAllocationId(int allocationId) { this.allocationId = allocationId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setRecommendationDate(LocalDateTime recommendationDate) { this.recommendationDate = recommendationDate; }
    public void setEquityPct(BigDecimal equityPct) { this.equityPct = equityPct; }
    public void setDebtPct(BigDecimal debtPct) { this.debtPct = debtPct; }
    public void setAlternativePct(BigDecimal alternativePct) { this.alternativePct = alternativePct; }
    public void setOtherDetails(String otherDetails) { this.otherDetails = otherDetails; }

    @Override
    public String toString() {
        return "PortfolioAllocation{" +
                "allocationId=" + allocationId +
                ", userId=" + userId +
                ", recommendationDate=" + recommendationDate +
                ", equityPct=" + equityPct +
                ", debtPct=" + debtPct +
                ", alternativePct=" + alternativePct +
                ", otherDetails='" + otherDetails + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortfolioAllocation that = (PortfolioAllocation) o;
        return allocationId == that.allocationId && userId == that.userId && recommendationDate.equals(that.recommendationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allocationId, userId, recommendationDate);
    }
}