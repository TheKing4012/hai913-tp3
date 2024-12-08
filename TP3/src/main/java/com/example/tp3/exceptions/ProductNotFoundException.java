package com.example.tp3.exceptions;

public class ProductNotFoundException extends Exception{

	public ProductNotFoundException() {
		super();
	}

	public ProductNotFoundException(String msg) {
		super(msg);
	}
}
