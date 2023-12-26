package com.dailymuse.microserviceauthentication.config.exeption;

public class BadTokenException extends RuntimeException {
        public BadTokenException(String message) {
            super(message);
        }
}
