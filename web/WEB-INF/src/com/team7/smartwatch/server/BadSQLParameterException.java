package com.team7.smartwatch.server;

public class BadSQLParameterException extends Exception {

	private static final long serialVersionUID = 9004377349862383937L;

	public BadSQLParameterException(String message) {
        super(message);
    }

    public BadSQLParameterException() {
        super();
    }
}
