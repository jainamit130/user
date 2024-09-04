package com.amit.converse.user.service;

import com.amit.converse.user.exceptions.ConverseException;
import com.amit.converse.user.model.RefreshToken;
import com.amit.converse.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken generateRefreshToken(){
        RefreshToken refreshToken= new RefreshToken();
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setCreationDate(Instant.now());
        return refreshTokenRepository.save(refreshToken);
    }

    public void validate(String refreshToken){
        refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ConverseException("Invalid refresh token! Please try logging in."));
    }

    public void deleteRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }
}

