package com.skilluser.user.serviceImpl;

import com.skilluser.user.model.Otp;
import com.skilluser.user.model.User;
import com.skilluser.user.repository.OtpRepository;
import com.skilluser.user.repository.UserRepository;
import com.skilluser.user.service.OtpService;
import com.skilluser.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private OtpService otpService;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new UsernameNotFoundException("User Not Found "+ id));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean changePassword(String email, String oldPassword, String newPassword)
    {
        User user = userRepository.findByEmail(email);
        if (user == null)
        {
            return false;
        }

        boolean passwordMatches = passwordEncoder.matches(oldPassword, user.getPassword());
        if (!passwordMatches)
        {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setPlainPassword(newPassword);
        userRepository.save(user);
        return true;
    }



    @Override
    public User forgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("SubAdmin Not Found with email: " + email);
        }

        String prefix = (user.getName() != null && user.getName().length() >= 2)
                ? user.getName().substring(0, 2)
                : "SP";
        String newPassword = generateOTP() + prefix;
        user.setPlainPassword(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        // Save into otp table

        String body = "<div style=\"max-width: 500px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; "
                + "font-family: Arial, sans-serif; background: #f9f9f9; padding: 20px; border-radius: 10px; "
                + "box-shadow: 0 4px 8px rgba(0,0,0,0.1);\">"
                + "<h1 style=\"text-align:center; color:#4A90E2;\">SkillPilots - Password Reset</h1>"
                + "<p style=\"font-size:16px; color:#333; text-align:center;\">We have generated a temporary password for your account.</p>"
                + "<p style=\"font-size:16px; color:#333; text-align:center;\">Use the password below to log in:</p>"
                + "<h2 style=\"text-align:center; color:#e74c3c;\">" + newPassword + "</h2>"
                + "<p style=\"text-align:center; font-size:14px; color:#666;\">After logging in, you must create a new password immediately for security reasons.</p>"
                + "<div style=\"margin-top:20px; text-align:center;\">"
                + "<small style=\"color:#aaa;\">If you did not request this, please ignore this email or contact our support team.</small>"
                + "</div>"
                + "</div>";


        otpService.sendVerificationEmail(user.getEmail(), "Password Reset Request", body);

        return user; // just return user, don’t save otp inside user
    }


    public String generateOTP()
    {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }


}
