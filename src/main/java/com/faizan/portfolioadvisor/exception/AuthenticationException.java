// src/main/java/com/faizan/portfolioadvisor/exception/AuthenticationException.java
package com.faizan.portfolioadvisor.exception;

// Use this for failed login attempts.
public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) {
        super(message);
    }
}