package com.elhadjium.PMBackend.exception;

public class PMEntityNotExistsException extends PMRuntimeException {
	private static final long serialVersionUID = 1L;

	public PMEntityNotExistsException(String message) {
		super(message, 404);
	}
	
	public PMEntityNotExistsException(String message, String details) {
		super(message, details, 404);
	}

}
