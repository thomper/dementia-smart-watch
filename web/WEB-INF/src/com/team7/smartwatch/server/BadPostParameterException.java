package com.team7.smartwatch.server;

public class BadPostParameterException extends Exception {
    
	private static final long serialVersionUID = -4479464034373195949L;

	public BadPostParameterException(String message) {
        super(message);
    }

    public BadPostParameterException() {
        super();
    }
}
