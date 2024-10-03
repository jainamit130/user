package com.amit.converse.user.controller;

import com.amit.converse.user.dto.LoginRequest;
import com.amit.converse.user.dto.RefreshTokenRequest;
import com.amit.converse.user.dto.ResponseDto;
import com.amit.converse.user.dto.SignUpRequest;
import com.amit.converse.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/converse/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequest request){
        return authService.signup(request);
    }

    @PostMapping("/login")
    public ResponseDto login(@RequestBody LoginRequest loginDto) {
        return authService.login(loginDto);
    }

    @PostMapping("/refreshToken")
    public ResponseDto refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        authService.logout(refreshTokenRequest);
        return new ResponseEntity<>("Logged out Successfully!",HttpStatus.OK);
    }
}
