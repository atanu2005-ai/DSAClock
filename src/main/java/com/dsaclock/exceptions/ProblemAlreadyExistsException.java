package com.dsaclock.exceptions;

public class ProblemAlreadyExistsException extends RuntimeException {

    public ProblemAlreadyExistsException(String message) {
        super(message);
    }
}
