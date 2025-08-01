// src/main/java/com/faizan/portfolioadvisor/exception/InvalidInputException.java
package com.faizan.portfolioadvisor.exception;

// Use this for invalid data provided by the user.
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}