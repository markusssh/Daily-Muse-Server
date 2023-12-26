package com.dailymuse.microservicenote.config.exception;

public class NoteDoesNotExistException extends RuntimeException {
    public NoteDoesNotExistException(String message) {
            super(message);
        }
}
