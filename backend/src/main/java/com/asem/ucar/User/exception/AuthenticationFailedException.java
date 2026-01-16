package com.asem.ucar.User.exception;

public class AuthenticationFailedException extends RuntimeException {
    public AuthenticationFailedException(String message) {
        super(message);
    }
    public static AuthenticationFailedException wrongEmailOrPassword() {
        return new AuthenticationFailedException("Wrong email or password");
    }
}
