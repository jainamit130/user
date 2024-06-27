package com.amit.converse.user.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ConverseUserNotFoundException extends UsernameNotFoundException {
    public ConverseUserNotFoundException(String username) {
        super("User name not found - " + username);
    }

    public ConverseUserNotFoundException() {
        super("User not found");
    }
}
