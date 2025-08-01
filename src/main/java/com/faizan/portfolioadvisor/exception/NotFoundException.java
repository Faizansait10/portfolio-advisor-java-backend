// src/main/java/com/faizan/portfolioadvisor/exception/NotFoundException.java
package com.faizan.portfolioadvisor.exception;

// Use this for situations where a requested resource (user, product, etc.) is not found.
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}