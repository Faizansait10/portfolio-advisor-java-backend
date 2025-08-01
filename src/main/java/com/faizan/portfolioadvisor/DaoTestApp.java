// src/main/java/com/faizan/portfolioadvisor/DaoTestApp.java
package com.faizan.portfolioadvisor;

import com.faizan.portfolioadvisor.dao.*;
import com.faizan.portfolioadvisor.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DaoTestApp {

    // Helper method for robust integer input
    private static int readInt(Scanner scanner) {
        while (true) {
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // Consume the leftover newline character
                return value;
            } catch (InputMismatchException e) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next(); // Consume the invalid input token
            }
        }
    }

    public static void main(String[] args) {
        // Instantiate all DAOs
        UserDAO userDAO = new UserDAO();
        FinancialProductDAO financialProductDAO = new FinancialProductDAO();
        UserRiskProfileDAO userRiskProfileDAO = new UserRiskProfileDAO();
        PortfolioAllocationDAO portfolioAllocationDAO = new PortfolioAllocationDAO();

        // Instantiate Scanner for user input
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Starting Interactive DAO Test Application ---\n");

        // Variables to hold objects for cross-DAO testing within a session
        // Initialize with null; these will be populated as user adds/retrieves them.
        User currentTestUser = null;
        FinancialProduct currentTestProduct = null;
        UserRiskProfile currentTestRiskProfile = null;
        PortfolioAllocation currentTestAllocation = null;


        while (true) {
            System.out.println("\n--- Main Menu: Choose DAO to Test ---");
            System.out.println("1. Test UserDAO");
            System.out.println("2. Test FinancialProductDAO");
            System.out.println("3. Test UserRiskProfileDAO");
            System.out.println("4. Test PortfolioAllocationDAO");
            System.out.println("5. Exit Application");
            System.out.print("Enter your choice: ");

            int choice = readInt(scanner);

            try {
                switch (choice) {
                    case 1:
                        currentTestUser = runUserDAOTests(scanner, userDAO, currentTestUser);
                        break;
                    case 2:
                        currentTestProduct = runFinancialProductDAOTests(scanner, financialProductDAO, currentTestProduct);
                        break;
                    case 3:
                        // UserRiskProfile and PortfolioAllocation tests require a user.
                        // If currentTestUser is null, prompt to create/select one.
                        if (currentTestUser == null || currentTestUser.getUserId() == 0) {
                            System.out.println("Please create or select a user first before testing Risk Profiles/Allocations.");
                            // Optionally, automatically run user test to create one or ask for ID
                            currentTestUser = runUserDAOTests(scanner, userDAO, currentTestUser);
                            if (currentTestUser == null || currentTestUser.getUserId() == 0) {
                                System.out.println("Cannot proceed without a valid user. Returning to main menu.");
                                break;
                            }
                        }
                        currentTestRiskProfile = runUserRiskProfileDAOTests(scanner, userRiskProfileDAO, currentTestUser, currentTestRiskProfile);
                        break;
                    case 4:
                        if (currentTestUser == null || currentTestUser.getUserId() == 0) {
                            System.out.println("Please create or select a user first before testing Risk Profiles/Allocations.");
                            currentTestUser = runUserDAOTests(scanner, userDAO, currentTestUser);
                            if (currentTestUser == null || currentTestUser.getUserId() == 0) {
                                System.out.println("Cannot proceed without a valid user. Returning to main menu.");
                                break;
                            }
                        }
                        currentTestAllocation = runPortfolioAllocationDAOTests(scanner, portfolioAllocationDAO, currentTestUser, currentTestAllocation);
                        break;
                    case 5:
                        System.out.println("Exiting application. Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // --- UserDAO Test Menu ---
    private static User runUserDAOTests(Scanner scanner, UserDAO userDAO, User testUser) {
        while (true) {
            System.out.println("\n--- UserDAO Test Menu ---");
            System.out.println("Current Test User (for update/delete): " + (testUser != null && testUser.getUserId() != 0 ? testUser.getName() + " (ID: " + testUser.getUserId() + ")" : "None selected/created"));
            System.out.println("1. Add New User");
            System.out.println("2. Get User by ID");
            System.out.println("3. Get User by Email");
            System.out.println("4. Get All Users");
            System.out.println("5. Update Existing User");
            System.out.println("6. Delete User");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = readInt(scanner);

            try {
                switch (choice) {
                    case 1:
                        System.out.print("   Enter Name: "); String name = scanner.nextLine();
                        System.out.print("   Enter Email: "); String email = scanner.nextLine();
                        System.out.print("   Enter Password Hash: "); String pass = scanner.nextLine();
                        System.out.print("   Enter Phone: "); String phone = scanner.nextLine();
                        System.out.print("   Enter Address: "); String address = scanner.nextLine();
                        User newUser = new User(name, email, pass, phone, address);
                        userDAO.addUser(newUser);
                        System.out.println("   Added User: " + newUser);
                        testUser = newUser; // Set this as the current test user
                        break;
                    case 2:
                        System.out.print("   Enter User ID to retrieve: "); int userId = readInt(scanner);
                        User retrievedById = userDAO.getUserById(userId);
                        if (retrievedById != null) System.out.println("   Retrieved User: " + retrievedById);
                        else System.out.println("   User with ID " + userId + " not found.");
                        break;
                    case 3:
                        System.out.print("   Enter User Email to retrieve: "); String userEmail = scanner.nextLine();
                        User retrievedByEmail = userDAO.getUserByEmail(userEmail);
                        if (retrievedByEmail != null) System.out.println("   Retrieved User: " + retrievedByEmail);
                        else System.out.println("   User with email '" + userEmail + "' not found.");
                        break;
                    case 4:
                        List<User> allUsers = userDAO.getAllUsers();
                        if (allUsers.isEmpty()) System.out.println("   No users in DB.");
                        else allUsers.forEach(u -> System.out.println("   - " + u));
                        break;
                    case 5:
                        if (testUser == null || testUser.getUserId() == 0) {
                            System.out.println("   Please add/retrieve a user first to set the current test user for update.");
                            break;
                        }
                        System.out.println("   Updating user with ID " + testUser.getUserId() + ". Current address: " + testUser.getAddress());
                        System.out.print("   Enter new address: "); testUser.setAddress(scanner.nextLine());
                        boolean updated = userDAO.updateUser(testUser);
                        System.out.println("   Update status: " + updated);
                        break;
                    case 6:
                        System.out.print("   Enter User ID to delete: "); int idToDelete = readInt(scanner);
                        boolean deleted = userDAO.deleteUser(idToDelete);
                        System.out.println("   Delete status: " + deleted);
                        if (testUser != null && testUser.getUserId() == idToDelete) {
                            testUser = null; // Clear current test user if deleted
                        }
                        break;
                    case 0: return testUser; // Back to main menu
                    default: System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.err.println("Error during UserDAO operation: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // --- FinancialProductDAO Test Menu ---
    private static FinancialProduct runFinancialProductDAOTests(Scanner scanner, FinancialProductDAO financialProductDAO, FinancialProduct testProduct) {
        while (true) {
            System.out.println("\n--- FinancialProductDAO Test Menu ---");
            System.out.println("Current Test Product (for update/delete): " + (testProduct != null && testProduct.getProductId() != 0 ? testProduct.getName() + " (ID: " + testProduct.getProductId() + ")" : "None selected/created"));
            System.out.println("1. Add New Product");
            System.out.println("2. Get Product by ID");
            System.out.println("3. Get All Products");
            System.out.println("4. Update Existing Product");
            System.out.println("5. Delete Product");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = readInt(scanner);

            try {
                switch (choice) {
                    case 1:
                        System.out.print("   Enter Product Name: "); String name = scanner.nextLine();
                        System.out.print("   Enter Product Type: "); String type = scanner.nextLine();
                        System.out.print("   Enter Description: "); String desc = scanner.nextLine();
                        System.out.print("   Enter Risk Level: "); String risk = scanner.nextLine();
                        System.out.print("   Enter Expected Return Rate (e.g., 8.50): "); BigDecimal ret = new BigDecimal(scanner.nextLine());
                        System.out.print("   Enter Minimum Investment (e.g., 1000.00): "); BigDecimal minInv = new BigDecimal(scanner.nextLine());
                        FinancialProduct newProduct = new FinancialProduct(name, type, desc, risk, ret, minInv);
                        financialProductDAO.addFinancialProduct(newProduct);
                        System.out.println("   Added Product: " + newProduct);
                        testProduct = newProduct;
                        break;
                    case 2:
                        System.out.print("   Enter Product ID to retrieve: "); int productId = readInt(scanner);
                        FinancialProduct retrievedProduct = financialProductDAO.getFinancialProductById(productId);
                        if (retrievedProduct != null) System.out.println("   Retrieved Product: " + retrievedProduct);
                        else System.out.println("   Product with ID " + productId + " not found.");
                        break;
                    case 3:
                        List<FinancialProduct> allProducts = financialProductDAO.getAllFinancialProducts();
                        if (allProducts.isEmpty()) System.out.println("   No products in DB.");
                        else allProducts.forEach(fp -> System.out.println("   - " + fp));
                        break;
                    case 4:
                        if (testProduct == null || testProduct.getProductId() == 0) {
                            System.out.println("   Please add/retrieve a product first to set the current test product for update.");
                            break;
                        }
                        System.out.println("   Updating product with ID " + testProduct.getProductId() + ". Current return rate: " + testProduct.getExpectedReturnRate());
                        System.out.print("   Enter new expected return rate: "); testProduct.setExpectedReturnRate(new BigDecimal(scanner.nextLine()));
                        boolean updated = financialProductDAO.updateFinancialProduct(testProduct);
                        System.out.println("   Update status: " + updated);
                        break;
                    case 5:
                        System.out.print("   Enter Product ID to delete: "); int idToDelete = readInt(scanner);
                        boolean deleted = financialProductDAO.deleteFinancialProduct(idToDelete);
                        System.out.println("   Delete status: " + deleted);
                        if (testProduct != null && testProduct.getProductId() == idToDelete) {
                            testProduct = null;
                        }
                        break;
                    case 0: return testProduct;
                    default: System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.err.println("Error during FinancialProductDAO operation: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // --- UserRiskProfileDAO Test Menu ---
    private static UserRiskProfile runUserRiskProfileDAOTests(Scanner scanner, UserRiskProfileDAO userRiskProfileDAO, User user, UserRiskProfile testRiskProfile) {
        while (true) {
            System.out.println("\n--- UserRiskProfileDAO Test Menu (for User ID: " + user.getUserId() + ") ---");
            System.out.println("Current Test Risk Profile (for update/delete): " + (testRiskProfile != null && testRiskProfile.getRiskProfileId() != 0 ? testRiskProfile.getPredictedRiskCategory() + " (ID: " + testRiskProfile.getRiskProfileId() + ")" : "None selected/created"));
            System.out.println("1. Add New Risk Profile");
            System.out.println("2. Get Latest Risk Profile for User");
            System.out.println("3. Get All Risk Profiles for User");
            System.out.println("4. Update Existing Risk Profile");
            System.out.println("5. Delete Risk Profile");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = readInt(scanner);

            try {
                switch (choice) {
                    case 1:
                        System.out.print("   Enter Predicted Risk Category: "); String category = scanner.nextLine();
                        System.out.print("   Enter Confidence Score (e.g., 0.85): "); BigDecimal conf = new BigDecimal(scanner.nextLine());
                        System.out.print("   Enter Age: "); Integer age = readInt(scanner);
                        System.out.print("   Enter Income in Lakhs: "); BigDecimal income = new BigDecimal(scanner.nextLine());
                        System.out.print("   Enter Investment Experience Years: "); Integer exp = readInt(scanner);
                        System.out.print("   Enter Financial Goal: "); String goal = scanner.nextLine();
                        UserRiskProfile newProfile = new UserRiskProfile(user.getUserId(), category, conf, age, income, exp, goal);
                        userRiskProfileDAO.addUserRiskProfile(newProfile);
                        System.out.println("   Added Risk Profile: " + newProfile);
                        testRiskProfile = newProfile;
                        break;
                    case 2:
                        UserRiskProfile latestProfile = userRiskProfileDAO.getLatestUserRiskProfile(user.getUserId());
                        if (latestProfile != null) System.out.println("   Latest Risk Profile: " + latestProfile);
                        else System.out.println("   No risk profile found for User ID " + user.getUserId() + ".");
                        break;
                    case 3:
                        List<UserRiskProfile> allProfiles = userRiskProfileDAO.getUserRiskProfilesByUserId(user.getUserId());
                        if (allProfiles.isEmpty()) System.out.println("   No risk profiles in DB for User ID " + user.getUserId() + ".");
                        else allProfiles.forEach(rp -> System.out.println("   - " + rp));
                        break;
                    case 4:
                        if (testRiskProfile == null || testRiskProfile.getRiskProfileId() == 0) {
                            System.out.println("   Please add/retrieve a risk profile first to set the current test profile for update.");
                            break;
                        }
                        System.out.println("   Updating risk profile with ID " + testRiskProfile.getRiskProfileId() + ". Current confidence: " + testRiskProfile.getConfidenceScore());
                        System.out.print("   Enter new confidence score: "); testRiskProfile.setConfidenceScore(new BigDecimal(scanner.nextLine()));
                        boolean updated = userRiskProfileDAO.updateUserRiskProfile(testRiskProfile);
                        System.out.println("   Update status: " + updated);
                        break;
                    case 5:
                        System.out.print("   Enter Risk Profile ID to delete: "); int idToDelete = readInt(scanner);
                        boolean deleted = userRiskProfileDAO.deleteUserRiskProfile(idToDelete);
                        System.out.println("   Delete status: " + deleted);
                        if (testRiskProfile != null && testRiskProfile.getRiskProfileId() == idToDelete) {
                            testRiskProfile = null;
                        }
                        break;
                    case 0: return testRiskProfile;
                    default: System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.err.println("Error during UserRiskProfileDAO operation: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // --- PortfolioAllocationDAO Test Menu ---
    private static PortfolioAllocation runPortfolioAllocationDAOTests(Scanner scanner, PortfolioAllocationDAO portfolioAllocationDAO, User user, PortfolioAllocation testAllocation) {
        while (true) {
            System.out.println("\n--- PortfolioAllocationDAO Test Menu (for User ID: " + user.getUserId() + ") ---");
            System.out.println("Current Test Allocation (for update/delete): " + (testAllocation != null && testAllocation.getAllocationId() != 0 ? "ID: " + testAllocation.getAllocationId() + " (" + testAllocation.getEquityPct() + "% Equity)" : "None selected/created"));
            System.out.println("1. Add New Allocation");
            System.out.println("2. Get Allocations by User ID");
            System.out.println("3. Get Allocation by ID");
            System.out.println("4. Update Existing Allocation");
            System.out.println("5. Delete Allocation");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");

            int choice = readInt(scanner);

            try {
                switch (choice) {
                    case 1:
                        System.out.print("   Enter Equity Percentage (e.g., 0.50): "); BigDecimal equity = new BigDecimal(scanner.nextLine());
                        System.out.print("   Enter Debt Percentage (e.g., 0.40): "); BigDecimal debt = new BigDecimal(scanner.nextLine());
                        System.out.print("   Enter Alternative Percentage (e.g., 0.10): "); BigDecimal alt = new BigDecimal(scanner.nextLine());
                        System.out.print("   Enter Other Details: "); String details = scanner.nextLine();
                        PortfolioAllocation newAlloc = new PortfolioAllocation(user.getUserId(), equity, debt, alt, details);
                        portfolioAllocationDAO.addPortfolioAllocation(newAlloc);
                        System.out.println("   Added Allocation: " + newAlloc);
                        testAllocation = newAlloc;
                        break;
                    case 2:
                        List<PortfolioAllocation> allAllocations = portfolioAllocationDAO.getPortfolioAllocationsByUserId(user.getUserId());
                        if (allAllocations.isEmpty()) System.out.println("   No allocations in DB for User ID " + user.getUserId() + ".");
                        else allAllocations.forEach(pa -> System.out.println("   - " + pa));
                        break;
                    case 3:
                        System.out.print("   Enter Allocation ID to retrieve: "); int allocId = readInt(scanner);
                        PortfolioAllocation retrievedById = portfolioAllocationDAO.getPortfolioAllocationById(allocId);
                        if (retrievedById != null) System.out.println("   Retrieved Allocation: " + retrievedById);
                        else System.out.println("   Allocation with ID " + allocId + " not found.");
                        break;
                    case 4:
                        if (testAllocation == null || testAllocation.getAllocationId() == 0) {
                            System.out.println("   Please add/retrieve an allocation first to set the current test allocation for update.");
                            break;
                        }
                        System.out.println("   Updating allocation with ID " + testAllocation.getAllocationId() + ". Current details: " + testAllocation.getOtherDetails());
                        System.out.print("   Enter new Other Details: "); testAllocation.setOtherDetails(scanner.nextLine());
                        boolean updated = portfolioAllocationDAO.updatePortfolioAllocation(testAllocation);
                        System.out.println("   Update status: " + updated);
                        break;
                    case 5:
                        System.out.print("   Enter Allocation ID to delete: "); int idToDelete = readInt(scanner);
                        boolean deleted = portfolioAllocationDAO.deletePortfolioAllocation(idToDelete);
                        System.out.println("   Delete status: " + deleted);
                        if (testAllocation != null && testAllocation.getAllocationId() == idToDelete) {
                            testAllocation = null;
                        }
                        break;
                    case 0: return testAllocation;
                    default: System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.err.println("Error during PortfolioAllocationDAO operation: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}