package com.elhadjium.PMBackend.exception;

import org.springframework.web.client.HttpStatusCodeException;

public class PMEntityNotExistsException extends PMRuntimeException {
	private static final long serialVersionUID = 1L;

	public PMEntityNotExistsException(String message) {
		super(message, 404);
	}
	
	public PMEntityNotExistsException(String message, String details) {
		super(message, null, 404);
	}

}
