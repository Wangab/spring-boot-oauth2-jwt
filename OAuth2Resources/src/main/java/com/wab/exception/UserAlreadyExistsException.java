package com.wab.exception;

public class UserAlreadyExistsException extends Exception {
    public UserAlreadyExistsException() {
        super("Exception: User already exists");
    }
}
