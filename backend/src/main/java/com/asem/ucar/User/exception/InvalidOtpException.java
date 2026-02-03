package com.asem.ucar.User.exception;

public class InvalidOtpException extends RuntimeException {
    public InvalidOtpException() {
        super("Invalid email or OTP");
    }

    public InvalidOtpException(String message) {
        super(message);
    }
}
