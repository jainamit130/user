package com.amit.converse.user.controller;

import com.amit.converse.user.dto.ProfileDto;
import com.amit.converse.user.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class UserActivityController {

    private ProfileService profileService;

    @GetMapping("/")
    public ResponseEntity<ProfileDto> getProfile(String username){
        return new ResponseEntity<ProfileDto>(profileService.getProfile(username), HttpStatus.OK);
    }

    @PostMapping("/about")
    public ResponseEntity<ProfileDto> updateAbout(Long userId, String about) {
        return new ResponseEntity<ProfileDto>(profileService.updateAbout(userId,about), HttpStatus.OK);
    }
}
