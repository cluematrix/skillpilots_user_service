package com.skilluser.user.service;

import com.skilluser.user.model.Otp;
import com.skilluser.user.model.User;

public interface OtpService {

//    public Otp generateOtp(User user);
//
//    public boolean validateOtp(User user, String inputOtp);

    public Otp generateOtp(User user);

  //  public boolean validateOtp(User user, String inputOtp);

    public void sendVerificationEmail(String toEmail, String subject, String content);

}
