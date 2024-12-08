package com.example.tp3.exceptions;

public class UserBadCredentialsException extends Exception{

	public UserBadCredentialsException() {
		super();
	}

	public UserBadCredentialsException(String msg) {
		super(msg);
	}
}
