package com.dsaclock.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //combines the idea of controller advice and response body
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class) //catches the exception for user not found [while authentication]
    public ResponseEntity<?> handleUserNotFound(UsernameNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) //returns the not found exception
                .body(ex.getMessage());
    }
    
    @ExceptionHandler(UserNotFoundException.class) //catches the exception for user not found [other than authentication]
    public ResponseEntity<?> handleUserNotFound(UserNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) //returns the not found exception
                .body(ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class) //handles exception if user already exists
    public ResponseEntity<?> handlesUserAlreadyExists(UserAlreadyExistsException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT) //returns exception if user already exists
                .body(ex.getMessage());
    }

    @ExceptionHandler(UserProblemAlreadyExistsException.class)  //handles user problem confliction
    public ResponseEntity<?> handleUserProblemAlreadyExists(UserProblemAlreadyExistsException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT) //returns http conflict for duplicate user problem
                .body(ex.getMessage());
    }
    
    @ExceptionHandler(ProblemNotFoundException.class) //handles exception when problem doesn't exists
    public ResponseEntity<?> handleProblemNotFound(ProblemNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) //returns problem not found exception
                .body(ex.getMessage());
    }

    @ExceptionHandler(ProblemAlreadyExistsException.class) //handles exception if problem already exists
    public ResponseEntity<?> handlesProblemAlreadyExists(ProblemAlreadyExistsException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT) //returns problem already exists exception
                .body(ex.getMessage());
    }

    @ExceptionHandler(UserProblemNotFoundException.class) //handles if the problem doesn't belong to the user
    public ResponseEntity<?> handlesUserProblemNotFound(UserProblemNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) //returns user problem not found exception
                .body(ex.getMessage());
    }
    
}
