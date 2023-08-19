package com.friend.app.util.exception;

public class DebtNotFoundException extends RuntimeException{
    public DebtNotFoundException(String message) {
        super(message);
    }
}
