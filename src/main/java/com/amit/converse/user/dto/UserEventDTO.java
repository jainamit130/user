package com.amit.converse.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
public class UserEventDTO {
    private String userId;
    private String username;
    private String creationDate;
}