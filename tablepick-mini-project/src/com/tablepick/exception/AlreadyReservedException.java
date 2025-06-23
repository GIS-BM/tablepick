package com.tablepick.exception;

public class AlreadyReservedException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4192486205284969958L;

	public AlreadyReservedException(String message) {
		super(message);
	}
}
