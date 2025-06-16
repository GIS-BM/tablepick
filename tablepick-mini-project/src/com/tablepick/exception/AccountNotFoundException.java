package com.tablepick.exception;

public class AccountNotFoundException extends Exception {
	

	private static final long serialVersionUID = 2530463334168611351L;
	
	public AccountNotFoundException(String message) {
		super(message);
	}
}