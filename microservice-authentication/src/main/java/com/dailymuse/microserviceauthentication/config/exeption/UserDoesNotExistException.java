package com.dailymuse.microserviceauthentication.config.exeption;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(String message) {
        super(message);
    }
}
