package com.example.exception;

public class InvalidAccountException extends Exception{
    public InvalidAccountException(String message) {
        super(message);
    }
}
