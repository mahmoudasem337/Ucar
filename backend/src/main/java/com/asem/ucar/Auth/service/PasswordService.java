package com.asem.ucar.Auth.service;

import com.asem.ucar.Auth.dto.ResetPasswordRequest;
import com.asem.ucar.Auth.utils.OtpUtils;
import com.asem.ucar.User.exception.InvalidOtpException;
import com.asem.ucar.User.exception.UserNotFoundException;
import com.asem.ucar.User.model.User;
import com.asem.ucar.User.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

    private final UserRepository userRepository;
    private final OtpUtils otpUtils;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordService(UserRepository userRepository, OtpUtils otpUtils, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.otpUtils = otpUtils;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void sendResetOtp(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            String otp = otpUtils.generateOtp();
            otpUtils.saveOtp(email, otp);
            emailService.sendOtpEmail(email, otp);
        });
    }

    public void resetPassword(ResetPasswordRequest request) {
        try {
            boolean validOtp = otpUtils.verifyOtp(request.email(), request.otp());
            if (!validOtp) {
                throw new InvalidOtpException();
            }
            User user = userRepository.findByEmail(request.email())
                    .orElseThrow(UserNotFoundException::new);
            user.setPassword(passwordEncoder.encode(request.newPassword()));
            userRepository.save(user);
        } finally {
            otpUtils.clearOtp(request.email());
        }
    }

}
