package com.example.tp3.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserNotFoundExceptionAdvice {
	
	@ExceptionHandler({UserNotFoundException.class, UserBadCredentialsException.class})
	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String userNotFoundExceptionHandler(Exception e) {
		return String.format("{\"%s\": \"%s\"}", "error", e.getMessage());
	}
}
