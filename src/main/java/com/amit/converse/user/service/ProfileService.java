package com.amit.converse.user.service;

import com.amit.converse.user.dto.ProfileDto;
import com.amit.converse.user.exceptions.ConverseUserNotFoundException;
import com.amit.converse.user.model.User;
import com.amit.converse.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    public ProfileDto getProfile(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ConverseUserNotFoundException(username));
        return ProfileDto.builder().username(user.getUsername()).about(user.getAbout()).build();
    }

    public ProfileDto updateAbout(Long userId, String about) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ConverseUserNotFoundException());
        user.setAbout(about);
        User updatedUser = userRepository.save(user);
        return ProfileDto.builder().username(user.getUsername()).about(user.getAbout()).build();
    }
}
