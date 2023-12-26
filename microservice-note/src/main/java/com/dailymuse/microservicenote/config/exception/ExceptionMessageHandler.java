package com.dailymuse.microservicenote.config.exception;

public class ExceptionMessageHandler {
    public static final String NOTE_ALREADY_EXISTS
            = "Note of this day already exists! Use PUT method instead";
    public static final String NOTE_DOES_NOT_EXIST
            = "Note does not exist! Use POST method instead";
}
