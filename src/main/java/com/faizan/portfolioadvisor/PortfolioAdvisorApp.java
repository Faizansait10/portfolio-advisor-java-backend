// src/main/java/com/faizan/portfolioadvisor/PortfolioAdvisorApp.java
package com.faizan.portfolioadvisor;

import com.faizan.portfolioadvisor.dao.PortfolioAllocationDAO;
import com.faizan.portfolioadvisor.dao.UserDAO;
import com.faizan.portfolioadvisor.dao.UserRiskProfileDAO;
import com.faizan.portfolioadvisor.exception.AuthenticationException;
import com.faizan.portfolioadvisor.exception.InvalidInputException;
import com.faizan.portfolioadvisor.exception.NotFoundException;
import com.faizan.portfolioadvisor.exception.PredictionException;
import com.faizan.portfolioadvisor.model.PortfolioAllocation;
import com.faizan.portfolioadvisor.model.User;
import com.faizan.portfolioadvisor.model.UserRiskProfile;
import com.faizan.portfolioadvisor.service.PortfolioAdvisorService;
import com.faizan.portfolioadvisor.service.UserService;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class PortfolioAdvisorApp {

    // Helper method for robust integer input
    private static int readInt(Scanner scanner) {
        while (true) {
            try {
                System.out.print("Enter your choice: ");
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline character
                return value;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number: ");
                scanner.next(); // Consume the invalid input token
            }
        }
    }

    public static void main(String[] args) {
        // Instantiate DAOs and Services
        UserDAO userDAO = new UserDAO();
        UserRiskProfileDAO userRiskProfileDAO = new UserRiskProfileDAO();
        PortfolioAllocationDAO portfolioAllocationDAO = new PortfolioAllocationDAO();

        UserService userService = new UserService(userDAO);
        PortfolioAdvisorService advisorService = new PortfolioAdvisorService(userRiskProfileDAO, portfolioAllocationDAO);

        // Scanner for user input
        Scanner scanner = new Scanner(System.in);
        User loggedInUser = null;

        System.out.println("--- Welcome to the Personalized Investment Portfolio Advisor ---");

        while (true) {
            if (loggedInUser == null) {
                // Main Menu (Not Logged In)
                System.out.println("\n--- Main Menu ---");
                System.out.println("1. Register User");
                System.out.println("2. Login User");
                System.out.println("3. Exit");
            } else {
                // User Dashboard (Logged In)
                System.out.println("\n--- Welcome, " + loggedInUser.getName() + " ---");
                System.out.println("1. Get My Portfolio Recommendation");
                System.out.println("2. View My Portfolio History");
                System.out.println("3. Logout");
            }

            int choice = readInt(scanner);

            try {
                if (loggedInUser == null) {
                    switch (choice) {
                        case 1:
                            loggedInUser = registerUser(scanner, userService);
                            break;
                        case 2:
                            loggedInUser = loginUser(scanner, userService);
                            break;
                        case 3:
                            System.out.println("Exiting application. Goodbye!");
                            scanner.close();
                            return;
                        default:
                            System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                    }
                } else {
                    switch (choice) {
                        case 1:
                            getPortfolioRecommendation(scanner, advisorService, loggedInUser);
                            break;
                        case 2:
                            viewPortfolioHistory(advisorService, loggedInUser);
                            break;
                        case 3:
                            loggedInUser = null; // Logout
                            System.out.println("You have been logged out.");
                            break;
                        default:
                            System.out.println("Invalid choice. Please enter a number between 1 and 3.");
                    }
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private static User registerUser(Scanner scanner, UserService userService) {
        System.out.println("\n--- User Registration ---");
        System.out.print("Enter full name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        try {
            User newUser = userService.registerUser(name, email, password, phone, address);
            System.out.println("Registration successful! Welcome, " + newUser.getName() + ".");
            return newUser;
        } catch (InvalidInputException e) {
            System.out.println("Registration failed: " + e.getMessage());
            return null;
        }
    }

    private static User loginUser(Scanner scanner, UserService userService) {
        System.out.println("\n--- User Login ---");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        try {
            User user = userService.userLogin(email, password);
            System.out.println("Login successful!");
            return user;
        } catch (AuthenticationException e) {
            System.out.println("Login failed: " + e.getMessage());
            return null;
        }
    }

    private static void getPortfolioRecommendation(Scanner scanner, PortfolioAdvisorService advisorService, User user) {
        System.out.println("\n--- Get Portfolio Recommendation ---");
        System.out.println("Please provide some details for risk analysis:");

        try {
            System.out.print("Enter your age: ");
            int age = readInt(scanner);
            System.out.print("Enter your annual income in lakhs (e.g., 15.50): ");
            BigDecimal income = new BigDecimal(scanner.nextLine());
            System.out.print("Enter your investment experience in years: ");
            int experience = readInt(scanner);
            System.out.print("Enter your primary financial goal (e.g., 'Retirement', 'Wealth Growth'): ");
            String goal = scanner.nextLine();

            System.out.println("\nContacting ML service for risk prediction...");
            UserRiskProfile riskProfile = advisorService.getAndSaveRiskPrediction(user, age, income, experience, goal);
            System.out.println("ML prediction successful! Result saved to database.");
            System.out.println("Predicted Risk Category: " + riskProfile.getPredictedRiskCategory() + " with confidence " + riskProfile.getConfidenceScore());

            System.out.println("\nGenerating portfolio recommendation...");
            PortfolioAllocation recommendation = advisorService.getRecommendedAllocation(riskProfile);
            System.out.println("Recommendation generated: " + recommendation.getOtherDetails());
            System.out.println("Equity: " + recommendation.getEquityPct() + "%, Debt: " + recommendation.getDebtPct() + "%, Alternative: " + recommendation.getAlternativePct() + "%");

            System.out.println("\nSaving the recommendation to your history...");
            advisorService.savePortfolioAllocation(recommendation);
            System.out.println("Recommendation saved successfully!");

        } catch (Exception e) {
            System.err.println("Error getting recommendation: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void viewPortfolioHistory(PortfolioAdvisorService advisorService, User user) {
        System.out.println("\n--- Your Portfolio History ---");
        try {
            List<PortfolioAllocation> history = advisorService.getUserPortfolioHistory(user);
            if (history.isEmpty()) {
                System.out.println("You have no saved recommendations yet.");
            } else {
                history.forEach(alloc -> System.out.println("- " + alloc.getRecommendationDate() + ": " + alloc.getOtherDetails() + " (Equity: " + alloc.getEquityPct() + "%)"));
            }
        } catch (Exception e) {
            System.err.println("Error retrieving history: " + e.getMessage());
            e.printStackTrace();
        }
    }
}