package com.skilluser.user.controller;

import java.time.Duration;
import java.util.HashMap;

import com.skilluser.user.model.User;
import com.skilluser.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skilluser.user.configuration.JwtUtil;
import com.skilluser.user.dto.LoginRequest;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        HashMap<Object, Object> response = new HashMap<>();

        try {
            // Load user by email
            User user = userRepository.findByEmail(loginRequest.getEmail());


            String rawPassword = loginRequest.getPassword();
            String storedPassword = user.getPassword(); // could be hashed or plain

            boolean isAuthenticated = false;

            // Case 1: If stored password is BCrypt hashed
            if (storedPassword != null && storedPassword.startsWith("$2a$")) {
                isAuthenticated = passwordEncoder.matches(rawPassword, storedPassword);
            }
            // Case 2: If stored password is plain text
            else if (storedPassword != null && storedPassword.equals(rawPassword)) {
                isAuthenticated = true;


                String newHashed = passwordEncoder.encode(rawPassword);
                user.setPassword(newHashed);
               // userRepository.save(user);
            }

            if (!isAuthenticated) {
                throw new BadCredentialsException("Invalid credentials");
            }

            // Build authentication object manually since we skipped authenticationManager
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwtToken = jwtUtils.generateToken(user);

            // set token into the cookie
            ResponseCookie cookie = ResponseCookie.from("jwt",jwtToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("Strick")
                    .build();

            response.put("token", jwtToken);
            response.put("user", user);
            response.put("authority", user.getAuthorities());

            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }


}
