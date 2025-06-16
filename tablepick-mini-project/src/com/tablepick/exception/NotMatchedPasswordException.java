package com.tablepick.exception;

public class NotMatchedPasswordException extends Exception {

	private static final long serialVersionUID = -8581279875210240942L;

	public NotMatchedPasswordException(String message) {
		super(message);
	}
}