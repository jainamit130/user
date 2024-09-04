package com.amit.converse.user.exceptions;

public class ConverseException extends RuntimeException {
    public ConverseException(String errorMessage) {
        super(errorMessage);
    }
}