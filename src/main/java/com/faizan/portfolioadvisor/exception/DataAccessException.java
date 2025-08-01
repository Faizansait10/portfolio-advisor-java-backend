// src/main/java/com/faizan/portfolioadvisor/exception/DataAccessException.java
package com.faizan.portfolioadvisor.exception;

// Use a custom exception for all database-related errors.
public class DataAccessException extends RuntimeException {
    public DataAccessException(String message) {
        super(message);
    }
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}