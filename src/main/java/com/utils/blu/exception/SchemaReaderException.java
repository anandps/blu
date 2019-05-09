package com.utils.blu.exception;

public class SchemaReaderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6078553263537838796L;

	/**
	 * This throws exception if no sufficient argument is passed in
	 * 
	 * @param message
	 */
	public SchemaReaderException(String message) {
		super(message);
	}

	public SchemaReaderException(String message, Exception e) {
		super(message, e);
	}

}