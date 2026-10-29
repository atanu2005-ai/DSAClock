package com.dsaclock.services;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final OtpService otpService;

    public EmailService(JavaMailSender javaMailSender, OtpService otpService) {
        this.javaMailSender = javaMailSender;
        this.otpService = otpService;
    }

    public void sendOtp(String to) {
        String otp = otpService.generateAndStoreOtp(to);

        String subject = "DSAClock - Email verification";

        String body = "Your email verification OTP is " + otp +
                "\n\n This code will expire in 5 minutes" +
                "\n If you didn't request this, please ignore";

        sendEmail(to, subject, body);
        System.out.println("otp was sent");
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("debnath.atanu2005@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }
}
