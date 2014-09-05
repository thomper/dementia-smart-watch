package com.team7.smartwatchservlets;

@SuppressWarnings("serial")
public class BadPostParameterException extends Exception {
    
    public BadPostParameterException(String message) {
        super(message);
    }

    public BadPostParameterException() {
        super();
    }
}
