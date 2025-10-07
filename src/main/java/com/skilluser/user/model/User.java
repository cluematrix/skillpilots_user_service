package com.skilluser.user.model;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String plainPassword;
    private String email;
    private String otp;
    private String photo;

    @Lob
    @Column(length = 1048576)
    private byte[] signature;

    private Long department;
    private Long contact_no;

    private String teamName;
    private short type;
    private String gender;
    private Long commonId;
    private Long instituteId;

    private int hodverified = 0;

    private int collegeId;

    private String tempPass;  // CamelCase, can map to 'temp_pass' in DB
    private String emailIdCommon;

    @Column(name = "creation_date") // DB column mapping to 'creation_date'
    private LocalDate creationDate = LocalDate.now();

    private String expirationRsn;  // Camel case, map if needed

    @Column(name = "login_attempt")
    private Integer loginAttempt = 0;

    @Column(name = "last_failed_login_attempt")
    private LocalDateTime lastFailedLoginAttempt;

    private String notificationToken;

    @ManyToOne
    private Role roles;
    private Long companyId;

    private String role;
    private int verified;

    public boolean isAccountLocked() {
        if (this.lastFailedLoginAttempt == null) {
            return false;
        }
        return this.loginAttempt >= 3 && this.lastFailedLoginAttempt.plusHours(24).isAfter(LocalDateTime.now());
    }

    public boolean canUnlockAccount() {
        if (this.lastFailedLoginAttempt == null) {
            return false;
        }
        return this.loginAttempt >= 3 && this.lastFailedLoginAttempt.plusHours(24).isBefore(LocalDateTime.now());
    }

    public User() {

    }




    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roles.getName()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getName(){
        return username;
    }
}

