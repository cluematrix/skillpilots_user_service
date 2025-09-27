package com.skilluser.user.controller;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.skilluser.user.model.User;
import com.skilluser.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpResponse) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Load user by email
            User user = userRepository.findByEmail(loginRequest.getEmail());
            if (user == null) {
                throw new BadCredentialsException("User not found");
            }

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

                // Upgrade plain-text password to hashed password
                String newHashed = passwordEncoder.encode(rawPassword);
                user.setPassword(newHashed);
                userRepository.save(user);
            }

            if (!isAuthenticated) {
                throw new BadCredentialsException("Invalid credentials");
            }

            List<String> roles = user.getAuthorities().stream()
                    .map(gr -> gr.getAuthority()) // ROLE_COMPANY, ROLE_COLLEGE, etc.
                    .collect(Collectors.toList());

            // Manually set authentication
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String jwtToken = jwtUtils.generateToken(user.getId(), user.getEmail(), roles, user.getName(),user.getContact_no());


            // set token into the cookie
        /*    ResponseCookie cookie = ResponseCookie.from("jwt",jwtToken)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("Strick")
                    .build();

            response.put("token", jwtToken);*/

//            // Create secure HttpOnly cookie
//            ResponseCookie cookie = ResponseCookie.from("auth_token", jwtToken)
//                    .httpOnly(true)        // prevent JS access
//                    .secure(false)         // set true in production (HTTPS)
//                                // available for entire domain
//                    .maxAge(24 * 60 * 60)  // 1 day
//                    .sameSite("None")    // CSRF protection
//                    .build();
//
//
//            // Add cookie in response header
//            httpResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//            response.put("cooki")
            // Also return user details in JSON

            response.put("user", user);
            response.put("authority", roles);
            response.put("token",jwtToken);




            return ResponseEntity.ok()

                    .body(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username or password"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("auth_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)   // remove cookie
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/me")
    public ResponseEntity<?> validateTokenFromHeader(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of(
                    "valid", false,
                    "message", "Missing or invalid Authorization header"
            ));
        }

        // Extract token from header
        String token = authHeader.substring(7);

        if (token.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "valid", false,
                    "message", "Token is empty"
            ));
        }

        // Validate token
        if (!jwtUtils.isValidToken(token)) {
            return ResponseEntity.status(401).body(Map.of(
                    "valid", false,
                    "message", "Token is invalid or expired"
            ));
        }

        // Decode token
        Claims claims = jwtUtils.decodeToken(token);

        return ResponseEntity.ok(Map.of(
                "valid", true,
                "user", claims
        ));
    }

}
