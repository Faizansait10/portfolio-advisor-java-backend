// src/main/java/com/faizan/portfolioadvisor/service/PortfolioAdvisorService.java
package com.faizan.portfolioadvisor.service;

import com.faizan.portfolioadvisor.dao.PortfolioAllocationDAO;
import com.faizan.portfolioadvisor.dao.UserRiskProfileDAO;
import com.faizan.portfolioadvisor.exception.PredictionException;
import com.faizan.portfolioadvisor.model.PortfolioAllocation;
import com.faizan.portfolioadvisor.model.User;
import com.faizan.portfolioadvisor.model.UserRiskProfile;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import org.json.JSONObject; // <-- IMPORTANT: Requires Maven dependency

public class PortfolioAdvisorService {
    private final UserRiskProfileDAO userRiskProfileDAO;
    private final PortfolioAllocationDAO portfolioAllocationDAO;
    private final HttpClient httpClient;

    // Constructor-based Dependency Injection
    public PortfolioAdvisorService(UserRiskProfileDAO userRiskProfileDAO, PortfolioAllocationDAO portfolioAllocationDAO) {
        this.userRiskProfileDAO = userRiskProfileDAO;
        this.portfolioAllocationDAO = portfolioAllocationDAO;
        // Use the Java 11+ HttpClient
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Gets a risk prediction from the Python ML service and saves it to the database.
     * @param user The user object for whom to predict risk.
     * @param age The user's age.
     * @param incomeLakhs The user's income in lakhs.
     * @param investmentExperienceYears The user's investment experience in years.
     * @param financialGoal The user's financial goal.
     * @return The newly created UserRiskProfile object.
     */
    public UserRiskProfile getAndSaveRiskPrediction(User user, int age, BigDecimal incomeLakhs, int investmentExperienceYears, String financialGoal) {
        String jsonInput = String.format(
                "{\"age\":%d, \"income_lakhs\":%s, \"investment_experience_years\":%d, \"financial_goal\":\"%s\"}",
                age, incomeLakhs.toString(), investmentExperienceYears, financialGoal
        );

        // Build the HTTP POST request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5000/predict_risk"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonInput))
                .build();
        try {
            // Send the request and get the response
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            if (response.statusCode() != 200) {
                throw new PredictionException("ML service returned an error. Status code: " + response.statusCode() + " | Body: " + responseBody);
            }

            // Parse the JSON response from the Python API
            JSONObject jsonResponse = new JSONObject(responseBody);
            String predictedRiskCategory = jsonResponse.getString("predicted_risk_category");
            BigDecimal confidenceScore = jsonResponse.getBigDecimal("confidence_score");

            // Create the UserRiskProfile object and save it to the DB
            UserRiskProfile newProfile = new UserRiskProfile(
                    user.getUserId(), predictedRiskCategory, confidenceScore, age, incomeLakhs, investmentExperienceYears, financialGoal
            );
            userRiskProfileDAO.addUserRiskProfile(newProfile);
            return newProfile;

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PredictionException("Failed to connect to the ML prediction service.", e);
        }
    }

    /**
     * Gets a recommended portfolio allocation based on a user's risk profile.
     * @param riskProfile The user's risk profile (from the DB).
     * @return A PortfolioAllocation object.
     */
    public PortfolioAllocation getRecommendedAllocation(UserRiskProfile riskProfile) {
        // Apply business rules based on the predicted risk category
        String riskCategory = riskProfile.getPredictedRiskCategory();
        BigDecimal equityPct, debtPct, alternativePct;
        String details;

        switch (riskCategory.toLowerCase()) {
            case "conservative":
                equityPct = new BigDecimal("0.20"); // 20%
                debtPct = new BigDecimal("0.70"); // 70%
                alternativePct = new BigDecimal("0.10"); // 10%
                details = "Conservative portfolio for low-risk, stable returns.";
                break;
            case "moderate":
                equityPct = new BigDecimal("0.50"); // 50%
                debtPct = new BigDecimal("0.40"); // 40%
                alternativePct = new BigDecimal("0.10"); // 10%
                details = "Moderate portfolio for balanced growth and risk.";
                break;
            case "aggressive":
                equityPct = new BigDecimal("0.80"); // 80%
                debtPct = new BigDecimal("0.10"); // 10%
                alternativePct = new BigDecimal("0.10"); // 10%
                details = "Aggressive portfolio for high growth potential.";
                break;
            default:
                // Fallback to a default if the category is unknown
                equityPct = new BigDecimal("0.30");
                debtPct = new BigDecimal("0.60");
                alternativePct = new BigDecimal("0.10");
                details = "Default portfolio recommendation.";
                break;
        }

        // Create and return the allocation object (doesn't save to DB here)
        return new PortfolioAllocation(riskProfile.getUserId(), equityPct, debtPct, alternativePct, details);
    }

    public void savePortfolioAllocation(PortfolioAllocation allocation) {
        portfolioAllocationDAO.addPortfolioAllocation(allocation);
    }

    public List<PortfolioAllocation> getUserPortfolioHistory(User user) {
        return portfolioAllocationDAO.getPortfolioAllocationsByUserId(user.getUserId());
    }
}