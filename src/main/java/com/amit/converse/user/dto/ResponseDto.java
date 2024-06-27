package com.amit.converse.user.dto;

import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class ResponseDto {
    private String authenticationToken;
    private String username;
    private Long userId;
    private Instant expiresAt;
    private String refreshToken;
}