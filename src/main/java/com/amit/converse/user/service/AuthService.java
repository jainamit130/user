package com.amit.converse.user.service;

import com.amit.converse.user.dto.LoginRequest;
import com.amit.converse.user.dto.ResponseDto;
import com.amit.converse.user.dto.SignUpRequest;
import com.amit.converse.user.dto.UserEventDTO;
import com.amit.converse.user.exceptions.ConverseUserNotFoundException;
import com.amit.converse.user.exceptions.converseException;
import com.amit.converse.user.model.User;
import com.amit.converse.user.model.VerificationToken;
import com.amit.converse.user.repository.UserRepository;
import com.amit.converse.user.repository.VerificationTokenRepository;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "user-events";

    public ResponseEntity<String> signup(SignUpRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return new ResponseEntity<>("Username is taken!!", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreationDate(Instant.now());
        user.setVerified(false);

        User savedUser = userRepository.save(user);
        String token = generateVerificationToken(user);
        //Use the below config along with google SMTP config
//        NotificationEmail notificationEmail= new NotificationEmail("Reddit: Please Activate your Account", user.getEmail(), "Thank you for signing up to Reddit /n"+
//                "Please click on the below link to activate your account: "+
//                appConfig.getUrl()+"auth/activateAccount/"+token);
//        mailService.sendMail(notificationEmail);
        activateAccount(token);
        UserEventDTO userEventDTO = UserEventDTO.builder().username(savedUser.getUsername()).userId(savedUser.getUserId().toString()).build();
        String userEvent = new Gson().toJson(userEventDTO);
        kafkaTemplate.send(TOPIC, userEvent);
        return new ResponseEntity<>("User Registration Successful", HttpStatus.OK);
    }

    @Transactional
    public void activateAccount(String token) {
        Optional<VerificationToken> verificationToken=verificationTokenRepository.findByToken(token);;
        verificationToken.ifPresentOrElse((authenticationToken) -> {
            Optional<User> user=userRepository.findById(authenticationToken.getUser().getUserId());
            user.ifPresentOrElse((UserToVerify) -> {
                UserToVerify.setVerified(true);
                userRepository.save(UserToVerify);
            },() -> {throw new converseException("No such account found. Please signup to register your account");});
        }, () -> { throw new converseException("Invalid token"); });
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public ResponseDto login(LoginRequest loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                        loginDto.getPassword()));
        User user = getUserFromUsername(loginDto.getUsername());
        String token = jwtService.generateToken(user);
        return ResponseDto.builder()
                .authenticationToken(token)
                .username(loginDto.getUsername())
                .userId(user.getUserId())
                .refreshToken(refreshTokenService.generateRefreshToken().getRefreshToken())
                .expiresAt(jwtService.getExpirationTime())
                .build();
    }

    private User getUserFromUsername(String username){
        var user =userRepository.findByUsername(username)
                .orElseThrow(() -> new ConverseUserNotFoundException(username));
        return user;
    }

}
