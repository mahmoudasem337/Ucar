package com.asem.ucar.Auth.utils;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpUtils {

    private final Map<String, String> otpCache = new ConcurrentHashMap<>();
    private final PasswordEncoder passwordEncoder;

    public OtpUtils(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String generateOtp() {
        int otp = new SecureRandom().nextInt(900_000) + 100_000;
        return String.valueOf(otp);
    }

    public void saveOtp(String email, String otp) {
        otpCache.put(email, passwordEncoder.encode(otp));
    }

    public String getHashedOtp(String email) {
        return otpCache.get(email);
    }

    public boolean verifyOtp(String email, String otp) {
        String hashedOtp = getHashedOtp(email);
        return hashedOtp != null && passwordEncoder.matches(otp, hashedOtp);
    }

    public void clearOtp(String email) {
        otpCache.remove(email);
    }
}
