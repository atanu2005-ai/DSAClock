package com.dsaclock.exceptions;

public class UserProblemAlreadyExistsException extends RuntimeException{

    public UserProblemAlreadyExistsException(String message) {
        super(message);
    }
}
