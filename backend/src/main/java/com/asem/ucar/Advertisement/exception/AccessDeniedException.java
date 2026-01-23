package com.asem.ucar.Advertisement.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
    public static AccessDeniedException AccessDeniedException() {
        return new AccessDeniedException("You are not authorized to delete/update this advertisement");
    }
}
