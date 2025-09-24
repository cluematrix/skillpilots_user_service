package com.skilluser.user.controller;

import com.skilluser.user.dto.ChangePasswordRequest;
import com.skilluser.user.dto.UserDto;
import com.skilluser.user.model.User;
import com.skilluser.user.service.OtpService;
import com.skilluser.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    public final UserService userService;
    public final ModelMapper modelMapper;
    private final OtpService otpService;
    public UserController(UserService userService, ModelMapper modelMapper, OtpService otpService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.otpService = otpService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getByUserId(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setName(user.getUsername());
            dto.setEmail(user.getEmail());

            if (user.getRoles() != null) {
                dto.setRole(user.getRoles().getName());
            }

            dto.setCollegeId(user.getCollegeId());

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/getUsername")
    public User getUserByName(@RequestParam String username)
    {
        User byUsername = userService.findByUsername(username);
        return byUsername;
    }


    @PostMapping("/email")
    public ResponseEntity<String> sendVerificationEmail(
            @RequestParam("toEmail") String toEmail,
            @RequestParam("subject") String subject,
            @RequestParam("content") String content) {
        try {
            // Call the service to send the email
            otpService.sendVerificationEmail(toEmail, subject, content);
            return ResponseEntity.ok("Verification email sent successfully to " + toEmail);
        } catch (Exception e) {
            // Handle error and return appropriate response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send verification email: " + e.getMessage());
        }
    }


    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request)
    {
        boolean changed = userService.changePassword(request.getEmail(),
                request.getOldPassword(),
                request.getNewPassword());

        if (changed)
        {
            return ResponseEntity.ok(Map.of("message", "Password Changed Successfully!"));
        }
        else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid email or old password"));
        }
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email)
    {
        try
        {
            User user = userService.forgotPassword(email);
            return ResponseEntity.ok(Map.of("message", "Temporary password sent to your email", "email", user.getEmail()));

        }
        catch (RuntimeException e)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }



}
