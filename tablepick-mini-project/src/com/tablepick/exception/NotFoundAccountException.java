package com.tablepick.exception;

public class NotFoundAccountException extends Exception {
	

	private static final long serialVersionUID = 2530463334168611351L;
	
	public NotFoundAccountException(String message) {
		super(message);
	}
}