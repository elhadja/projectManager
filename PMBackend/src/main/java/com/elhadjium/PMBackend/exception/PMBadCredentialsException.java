package com.elhadjium.PMBackend.exception;

public class PMBadCredentialsException extends PMRuntimeException {
	private static final long serialVersionUID = 1L;

	public PMBadCredentialsException(String message) {
		super(message, 401);
	}

}
