package com.karpovich.homework.exceptions;

public class ConcurrencyException extends RuntimeException {
    public ConcurrencyException (String message){
        super (message);
    }
}
