package com.student.student_service.exception;

public class WrongDataTypeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WrongDataTypeException(String message) {
        super(message);
    }
}
