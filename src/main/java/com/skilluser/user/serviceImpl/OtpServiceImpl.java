
package com.skilluser.user.serviceImpl;

import com.skilluser.user.model.Otp;
import com.skilluser.user.model.User;
import com.skilluser.user.repository.OtpRepository;
import com.skilluser.user.service.OtpService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import jakarta.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpRepository otpRepository;
    private final JavaMailSender javaMailSender;


//    @Override
//    public Otp generateOtp(User user)
//    {
//        // Generate a 6-digit OTP
//        String otpValue = String.format("%06d", new Random().nextInt(999999));
//
//        // Set expiry to 5 minutes from now
//        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);
//
//        Otp otp = new Otp();
//        otp.setOtp(otpValue);
//
//        return otpRepository.save(otp);
//    }

//    @Override
//    public boolean validateOtp(User user, String inputOtp)
//    {
//        Optional<Otp> latestOtp = otpRepository.findTopByUserOrderByExpirationTimeDesc(user);
//
//        return latestOtp.isPresent() && latestOtp.get().isValid(inputOtp);
//    }

    @Override
    public Otp generateOtp(User user)
    {
        // Generate a 6-digit OTP
        String otpValue = String.format("%06d", new Random().nextInt(999999));

        // Set expiry to 5 minutes from now
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

        Otp otp = new Otp();
        otp.setOtp(otpValue);

        return otpRepository.save(otp);
    }

    /*@Override
    public boolean validateOtp(User user, String inputOtp)
    {
        Optional<Otp> latestOtp = otpRepository.findTopByUserOrderByExpirationTimeDesc(user);

        return latestOtp.isPresent() && latestOtp.get().isValid(inputOtp);
    }*/

    @Override
    public void sendVerificationEmail(String toEmail, String subject, String content)
    {
        try
        {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);

            String emailContent = content;

            helper.setText(emailContent, true);
            javaMailSender.send(message);
        }
        catch (MessagingException e)
        {
            e.printStackTrace();
        }
    }
}

