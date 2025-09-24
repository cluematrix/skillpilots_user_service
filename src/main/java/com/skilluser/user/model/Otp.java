package com.skilluser.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Table(name = "user_otp")
@Entity
public class Otp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String otp;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    private boolean used = false;

    private String email;

    public Otp() {}

    public Otp(String otp, LocalDateTime expirationTime) {
        this.otp = otp;
        this.expirationTime = expirationTime;

    }

    public Otp(String otpValue, LocalDateTime expiry, User user) {
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime);
    }

    public boolean isValid(String inputOtp) {
        return !isExpired() && !used && this.otp.equals(inputOtp);
    }

}
