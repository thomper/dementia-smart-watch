package com.team7.smartwatchservlets;

public class NullPostParameterException extends Exception {
    
    public NullPostParameterException(String message) {
        super(message);
    }

    public NullPostParameterException() {
        super();
    }
}
