package com.elhadjium.PMBackend.exception;

public class PMEntityExistsException extends PMRuntimeException {
	public PMEntityExistsException(String message) {
		super(message, 400);
	}

	private static final long serialVersionUID = 1L;

}
