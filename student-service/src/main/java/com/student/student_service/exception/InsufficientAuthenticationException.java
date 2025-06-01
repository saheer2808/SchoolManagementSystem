package com.student.student_service.exception;

public class InsufficientAuthenticationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InsufficientAuthenticationException(String msg) {
		super(msg);
	}

}
