package com.skilluser.user.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.skilluser.user.dto.LoginResponse;
import com.skilluser.user.fiegnclient.StudentEmploymentClient;
import com.skilluser.user.model.*;
import com.skilluser.user.repository.RecruitmentAccessRepository;
import com.skilluser.user.repository.UserRepository;
import com.skilluser.user.service.FeedbackFormService;
import com.skilluser.user.service.ModuleService;
import com.skilluser.user.service.UserService;
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
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private StudentEmploymentClient studentEmploymentClient;
    @Autowired
    private UserService userService;
    @Autowired
    private FeedbackFormService feedbackFormService;
    @Autowired
    private RecruitmentAccessRepository recruitmentAccessRepository;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse httpResponse) {
        Map<String, Object> response = new HashMap<>();

        try
        {
            // Load user by email
            User user = userRepository.findByEmail(loginRequest.getEmail());
            if (user == null)
            {
                throw new BadCredentialsException("User not found");
            }

            String rawPassword = loginRequest.getPassword();
            String storedPassword = user.getPassword(); // could be hashed or plain

            boolean isAuthenticated = false;

            // Case 1: If stored password is BCrypt hashed
            if (storedPassword != null && storedPassword.startsWith("$2a$"))
            {
                isAuthenticated = passwordEncoder.matches(rawPassword, storedPassword);
            }
            // Case 2: If stored password is plain text
            else if (storedPassword != null && storedPassword.equals(rawPassword))
            {
                isAuthenticated = true;

                // Upgrade plain-text password to hashed password
                String newHashed = passwordEncoder.encode(rawPassword);
                user.setPassword(newHashed);
                userRepository.save(user);
            }

            if (!isAuthenticated)
            {
                throw new BadCredentialsException("Invalid credentials");
            }


            //  Recruiter Expiry Check
          /*  if ("COMPANY_RECRUITER".equalsIgnoreCase(user.getRole()))
            {
                RecruitmentAccess access
                        = recruitmentAccessRepository.findByUserIdAndActiveTrue(user.getId());

                if (access == null)
                {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of(
                                    "message",
                                    "Recruiter access not assigned. Contact college admin."
                            ));
                }

                if (access.getExpiryDate().isBefore(LocalDate.now()))
                {
                    // Auto deactivate
                    access.setActive(false);
                    recruitmentAccessRepository.save(access);

                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of(
                                    "message",
                                    "Your recruiter access has expired. Please contact college admin."
                            ));
                }
            }
*/


            if ("COMPANY_RECRUITER".equalsIgnoreCase(user.getRole())) {

                List<RecruitmentAccess> accessList =
                        recruitmentAccessRepository
                                .findByUserIdAndActiveTrue(user.getId());

                if (accessList == null || accessList.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of(
                                    "message",
                                    "No placement access assigned. Contact admin."
                            ));
                }

                boolean hasValidAccess = accessList.stream()
                        .anyMatch(a ->
                                a.getExpiryDate() != null &&
                                        !a.getExpiryDate().isBefore(LocalDate.now())
                        );

                if (!hasValidAccess) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(Map.of(
                                    "message",
                                    "All recruiter access expired. Please contact college admin."
                            ));
                }

                // Optional: Send accessible placement list
                List<Long> validPlacementIds = accessList.stream()
                        .filter(a ->
                                a.getExpiryDate() != null &&
                                        !a.getExpiryDate().isBefore(LocalDate.now()))
                        .map(RecruitmentAccess::getPlacementId)
                        .collect(Collectors.toList());

                response.put("accessiblePlacements", validPlacementIds);
            }


            List<String> roles = user.getAuthorities().stream()
                    .map(gr -> gr.getAuthority()) // ROLE_COMPANY, ROLE_COLLEGE, etc.
                    .collect(Collectors.toList());

            // Manually set authentication
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);


            // Generate JWT token
            String jwtToken = jwtUtils.generateToken(user.getId(), user.getEmail(), roles, user.getName(), user.getContact_no(), user.getCollegeId(), user.getCompanyId(), user.getDepartment());

            // Also return user details in JSON
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setUserId(user.getId());
            loginResponse.setCollegeId(user.getCollegeId());
            loginResponse.setCompanyId(user.getCompanyId());
            loginResponse.setEmail(user.getEmail());
            loginResponse.setUsername(user.getName());
            loginResponse.setUser_role(roles);
            loginResponse.setDepartment(user.getDepartment());
            loginResponse.setHodverified(user.getHodverified());
            loginResponse.setContact_no(user.getContact_no());
            loginResponse.setPhoto(user.getPhoto());
            response.put("user", loginResponse);

            response.put("token", jwtToken);
            response.put("user_role", roles);
            Long roleIds = user.getRoles().getId();
            response.put("roleId", roleIds);



            Map<String, Object> permissionsForUser = moduleService.getPermissionsForUser(user.getId());
            response.put("permission",permissionsForUser);

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

    // decode token shrunkhal 26/sept
//    @GetMapping("/me")
//    public ResponseEntity<?> validateTokenFromHeader(HttpServletRequest request) {
//        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return ResponseEntity.status(401).body(Map.of(
//                    "valid", false,
//                    "message", "Missing or invalid Authorization header"
//            ));
//        }
//
//        // Extract token from header
//        String token = authHeader.substring(7);
//
//        if (token.isEmpty()) {
//            return ResponseEntity.badRequest().body(Map.of(
//                    "valid", false,
//                    "message", "Token is empty"
//            ));
//        }
//
//        // Validate token
//        if (!jwtUtils.isValidToken(token)) {
//            return ResponseEntity.status(401).body(Map.of(
//                    "valid", false,
//                    "message", "Token is invalid or expired"
//            ));
//        }
//
//        // Decode token
//        Claims claims = jwtUtils.decodeToken(token);
//        Long userId = claims.get("userId",Long.class);
//        claims.get("deptId", Long.class);
//        Map<String, Object> studentData =
//                studentEmploymentClient.getWorkStatus(userId);
//        String username = userRepository.findById(userId).map(User::getName).get();
//        // fetch permission
//        Map<String, Object> permissionsForUser = moduleService.getPermissionsForUser(userId);
//        boolean given = userService.hasStudentGivenTest(userId);
//
//        return ResponseEntity.ok(Map.of(
//                "valid", true,
//                "user", claims,
//                "permission",permissionsForUser,
//                "name",username,
//                "work_status",studentData,
//                "hasGivenSkillAssessment", given
//        ));
//    }

    @GetMapping("/me")
    public ResponseEntity<?> validateTokenFromHeader(HttpServletRequest request) {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of(
                    "valid", false,
                    "message", "Missing or invalid Authorization header"
            ));
        }

        String token = authHeader.substring(7);
        if (!jwtUtils.isValidToken(token)) {
            return ResponseEntity.status(401).body(Map.of(
                    "valid", false,
                    "message", "Token is invalid or expired"
            ));
        }

        Claims claims = jwtUtils.decodeToken(token);
        Long userId = claims.get("userId", Long.class);

        return ResponseEntity.ok(userService.buildProfile(userId, claims));
    }

    @PostMapping("/contact")
    public ResponseEntity<?> sendMessage( @RequestBody ContactRequest request) {
        userService.processContact(request);
        return ResponseEntity.ok("Message sent successfully");
    }
   /* @GetMapping("/me")
    public ResponseEntity<?> validateTokenFromHeader(HttpServletRequest request)
    {
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
        Long userId = claims.get("userId",Long.class);
        claims.get("deptId", Long.class);

        String username = userRepository.findById(userId).map(User::getName).get();
        // fetch permission
        Map<String, Object> permissionsForUser = moduleService.getPermissionsForUser(userId);

        Integer totalExperienceYears = studentEmploymentClient.getTotalExperience(userId);

        Map<String, Object> userMap = new HashMap<>(claims);    
        userMap.put("name", username);
        userMap.put("experience", totalExperienceYears);

        return ResponseEntity.ok(Map.of(
                "valid", true,
                "user", userMap,
                "permission",permissionsForUser,
                "name",username
        ));
    }*/
   // create feedback form
   @PostMapping("feedback")
   public ResponseEntity<Map<String,Object>> createFeedbackForm(@RequestBody FeedbackForm feedbackForm){
       Map<String, Object> response = new HashMap<>();
       try {
           FeedbackForm form = feedbackFormService.createFeedbackForm(feedbackForm);
           response.put("status", "success");
           response.put("data", form);
           return ResponseEntity.ok(response);

       } catch (RuntimeException e){
           response.put("status", "error");
           response.put("message", e.getMessage());
           response.put("errorType", e.getClass().getSimpleName());

           return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body(response);
       }
   }


    // get feedback form by Id
    @GetMapping("feedback/{id}")
    public ResponseEntity<FeedbackForm> getFeedbackFormById(@PathVariable  Long id){
        FeedbackForm form = feedbackFormService.getFeedbackFormById(id);
        return ResponseEntity.ok(form);
    }


    // get all Feedback forms
    @GetMapping("feedback")
    public ResponseEntity<List<FeedbackForm>> getAllFeedbackForms(){
        List<FeedbackForm> forms = feedbackFormService.getAllFeedbackForms();
        return ResponseEntity.ok(forms);
    }


    // delete feedback form by id
    @DeleteMapping("/feedback/{id}")
    public ResponseEntity<Map<String, String>> deleteFeedbackFormById(@PathVariable Long id){
        Map<String,String> response = new HashMap<>();
        try{
            feedbackFormService.deleteById(id);
            response.put("message","Feedback form is deleted successfully.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message",e.getMessage());
            return ResponseEntity.ok(response);
        }
    }



    // update Feedback form
    @PutMapping("feedback/{id}")
    public ResponseEntity<Map<String,String>> updateFeedbackForm(@RequestBody FeedbackForm feedbackForm,
                                                                 @PathVariable Long id)
    {
        Map<String,String> response = new HashMap<>();
        try{
            feedbackFormService.updateFeedbackForm(feedbackForm,id);
            response.put("message","Feedback form is updated successfully.");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("message",e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}
