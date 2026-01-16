package com.asem.ucar.Auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String to, String otp) {
        mailSender.send(createOtpMessage(to, otp));
    }

    private SimpleMailMessage createOtpMessage(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to);
        message.setSubject("Password Reset OTP");
        message.setText("""
                Your OTP is: %s
                This code will expire in 5 minutes.
                """.formatted(otp));
        return message;
    }
}

