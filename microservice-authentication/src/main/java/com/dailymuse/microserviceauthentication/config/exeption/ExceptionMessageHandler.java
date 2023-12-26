package com.dailymuse.microserviceauthentication.config.exeption;

public class ExceptionMessageHandler {
    public static final String USER_ALREADY_EXISTS
            = "User with this email already exists!";
    public static final String BAD_CREDENTIALS
            = "Wrong email or password";
    public static final String ACCOUNT_DISABLED
            = "The account is disabled";
    public static final String ACCOUNT_LOCKED
            = "The account is banned";
    public static final String BAD_TOKEN
            = "The authorization token is not valid";
}
