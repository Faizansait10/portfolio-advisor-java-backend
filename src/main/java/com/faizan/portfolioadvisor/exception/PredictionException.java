// src/main/java/com/faizan/portfolioadvisor/exception/PredictionException.java
package com.faizan.portfolioadvisor.exception;

// Use a custom exception for errors related to the ML prediction service.
public class PredictionException extends RuntimeException {
    public PredictionException(String message) {
        super(message);
    }
    public PredictionException(String message, Throwable cause) {
        super(message, cause);
    }
}