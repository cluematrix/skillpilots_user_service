package com.skilluser.user.repository;

import com.skilluser.user.model.Otp;
import com.skilluser.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp,Long> {


  public Optional<Otp> findTopByUserOrderByExpirationTimeDesc(User  user);


}
