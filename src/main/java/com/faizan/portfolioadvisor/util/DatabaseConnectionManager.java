package com.faizan.portfolioadvisor.util;

// src/main/java/com/faizan/portfolioadvisor/util/DatabaseConnectionManager.java

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionManager {
    private static final String DB_PROPERTIES_FILE = "database.properties";
    private static Properties properties = new Properties();

    // Static block: This block is executed exactly once when the class is loaded.
    // It's used here to load the database connection properties from the file.
    static {
        try (InputStream input = DatabaseConnectionManager.class.getClassLoader().getResourceAsStream(DB_PROPERTIES_FILE)) {
            if (input == null) {
                // If the properties file is not found, throw a runtime exception.
                // This indicates a critical configuration error.
                throw new IOException("Sorry, unable to find " + DB_PROPERTIES_FILE + " in classpath.");
            }
            properties.load(input); // Load properties from the file
        } catch (IOException ex) {
            // Print stack trace for debugging during development.
            ex.printStackTrace();
            // Re-throw as a RuntimeException, as the application cannot function without DB connection details.
            throw new RuntimeException("Failed to load database properties: " + ex.getMessage(), ex);
        }
    }

    /**
     * Establishes and returns a new database connection.
     * The connection parameters are loaded from database.properties.
     *
     * @return A new Connection object.
     * @throws SQLException If a database access error occurs or the URL is null.
     * @throws RuntimeException If database properties are incomplete.
     */
    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        // Basic validation for properties
        if (url == null || username == null || password == null) {
            throw new RuntimeException("Database connection properties (url, username, password) are incomplete in " + DB_PROPERTIES_FILE);
        }

        // DriverManager attempts to establish a connection to the given database URL.
        return DriverManager.getConnection(url, username, password);
    }

    // Optional: A main method for quick testing of connection manager
    public static void main(String[] args) {
        System.out.println("Attempting to get a database connection...");
        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            System.out.println("Connection successful! Database metadata: " + conn.getMetaData().getDatabaseProductName());
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        } catch (RuntimeException e) {
            System.err.println("Configuration error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}