package com.dsaclock.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProblemAlreadyExistsException extends RuntimeException {

    public ProblemAlreadyExistsException(String message) {
        super(message);
    }
}
