package com.utils.blu.exception;

public class InsufficientDataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6078553263537838796L;

	/**
	 * This throws exception if no sufficient argument is passed in
	 * 
	 * @param message
	 */
	public InsufficientDataException(String message) {
		super(message);
	}

	public InsufficientDataException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}