package com.friend.app.util.exception;

public class PersonUsernameNotUniqueException extends RuntimeException{

    public PersonUsernameNotUniqueException(String message) {
        super(message);
    }
}
