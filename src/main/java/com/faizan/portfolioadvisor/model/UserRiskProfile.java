// src/main/java/com/faizan/portfolioadvisor/model/UserRiskProfile.java
package com.faizan.portfolioadvisor.model;

import java.math.BigDecimal; // For DECIMAL data type from SQL
import java.time.LocalDateTime;
import java.util.Objects;

public class UserRiskProfile {
    private int riskProfileId;
    private int userId;
    private String predictedRiskCategory; // e.g., 'Conservative', 'Moderate', 'Aggressive'
    private LocalDateTime predictionDate;
    private BigDecimal confidenceScore; // Corresponds to DECIMAL(5,2)
    private Integer age; // Using Integer wrapper type to allow for null if not provided
    private BigDecimal incomeLakhs; // Corresponds to DECIMAL(10,2)
    private Integer investmentExperienceYears; // Using Integer wrapper type
    private String financialGoal; // e.g., 'Retirement', 'Home Purchase'

    // Default constructor
    public UserRiskProfile() {
    }

    // Constructor for creating new risk profiles (without ID initially)
    public UserRiskProfile(int userId, String predictedRiskCategory, BigDecimal confidenceScore, Integer age, BigDecimal incomeLakhs, Integer investmentExperienceYears, String financialGoal) {
        this.userId = userId;
        this.predictedRiskCategory = predictedRiskCategory;
        this.predictionDate = LocalDateTime.now(); // Set current time
        this.confidenceScore = confidenceScore;
        this.age = age;
        this.incomeLakhs = incomeLakhs;
        this.investmentExperienceYears = investmentExperienceYears;
        this.financialGoal = financialGoal;
    }

    // Constructor for retrieving existing risk profiles from DB (with ID and predictionDate)
    public UserRiskProfile(int riskProfileId, int userId, String predictedRiskCategory, LocalDateTime predictionDate, BigDecimal confidenceScore, Integer age, BigDecimal incomeLakhs, Integer investmentExperienceYears, String financialGoal) {
        this.riskProfileId = riskProfileId;
        this.userId = userId;
        this.predictedRiskCategory = predictedRiskCategory;
        this.predictionDate = predictionDate;
        this.confidenceScore = confidenceScore;
        this.age = age;
        this.incomeLakhs = incomeLakhs;
        this.investmentExperienceYears = investmentExperienceYears;
        this.financialGoal = financialGoal;
    }

    // --- Getters ---
    public int getRiskProfileId() { return riskProfileId; }
    public int getUserId() { return userId; }
    public String getPredictedRiskCategory() { return predictedRiskCategory; }
    public LocalDateTime getPredictionDate() { return predictionDate; }
    public BigDecimal getConfidenceScore() { return confidenceScore; }
    public Integer getAge() { return age; }
    public BigDecimal getIncomeLakhs() { return incomeLakhs; }
    public Integer getInvestmentExperienceYears() { return investmentExperienceYears; }
    public String getFinancialGoal() { return financialGoal; }

    // --- Setters ---
    public void setRiskProfileId(int riskProfileId) { this.riskProfileId = riskProfileId; }
    public void setUserId(int userId) { this.userId = userId; }
    public void setPredictedRiskCategory(String predictedRiskCategory) { this.predictedRiskCategory = predictedRiskCategory; }
    public void setPredictionDate(LocalDateTime predictionDate) { this.predictionDate = predictionDate; }
    public void setConfidenceScore(BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }
    public void setAge(Integer age) { this.age = age; }
    public void setIncomeLakhs(BigDecimal incomeLakhs) { this.incomeLakhs = incomeLakhs; }
    public void setInvestmentExperienceYears(Integer investmentExperienceYears) { this.investmentExperienceYears = investmentExperienceYears; }
    public void setFinancialGoal(String financialGoal) { this.financialGoal = financialGoal; }

    @Override
    public String toString() {
        return "UserRiskProfile{" +
                "riskProfileId=" + riskProfileId +
                ", userId=" + userId +
                ", predictedRiskCategory='" + predictedRiskCategory + '\'' +
                ", predictionDate=" + predictionDate +
                ", confidenceScore=" + confidenceScore +
                ", age=" + age +
                ", incomeLakhs=" + incomeLakhs +
                ", investmentExperienceYears=" + investmentExperienceYears +
                ", financialGoal='" + financialGoal + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRiskProfile that = (UserRiskProfile) o;
        return riskProfileId == that.riskProfileId && userId == that.userId && predictedRiskCategory.equals(that.predictedRiskCategory) && predictionDate.equals(that.predictionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(riskProfileId, userId, predictedRiskCategory, predictionDate);
    }
}