package com.amit.converse.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignUpRequest {
    private String email;
    @NotBlank(message = "Username is required")
    private String username;
    private String password;
}
