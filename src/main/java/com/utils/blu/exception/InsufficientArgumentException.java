package com.utils.blu.exception;

public class InsufficientArgumentException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6078553263537838796L;

	/**
	 * This throws exception if no sufficient argument is passed in
	 * 
	 * @param message
	 */
	public InsufficientArgumentException(String message) {
		super(message);
	}

	public InsufficientArgumentException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}