package com.team7.smartwatch.server;

@SuppressWarnings("serial")
public class BadPostParameterException extends Exception {
    
    public BadPostParameterException(String message) {
        super(message);
    }

    public BadPostParameterException() {
        super();
    }
}
