package com.student.student_service.exception;

public class InvalidJsonDataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidJsonDataException(String message) {
        super(message);
    }
}
