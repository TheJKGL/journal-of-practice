package com.malakhov.journalofpractice.exception;

public class ResourceAlreadyExistException extends RuntimeException {

    public ResourceAlreadyExistException(String message) {
        super(message);
    }
}
