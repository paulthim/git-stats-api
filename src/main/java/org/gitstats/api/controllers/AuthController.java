package org.gitstats.api.controllers;

import org.gitstats.api.models.User;
import org.gitstats.api.models.dto.JwtAuthenticationResponse;
import org.gitstats.api.models.dto.LoginRequest;
import org.gitstats.api.models.dto.UserRequest;
import org.gitstats.api.repository.UserRepository;
import org.gitstats.api.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    User signup(@Valid @RequestBody UserRequest userRequest) {
        Optional<User> existingUser = userRepository.findByEmail(userRequest.getEmail());
        User user = existingUser.isPresent() ? existingUser.get().update(userRequest) : new User(userRequest);
        if (userRequest.getPassword() != null && userRequest.getPassword().trim().length() > 1) {
            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }
        return userRepository.save(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
}
