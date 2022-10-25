package com.elhadjium.PMBackend.exception;

public class PMInvalidInputDTO extends PMRuntimeException {
	public PMInvalidInputDTO(String message) {
		super(message, 400);
	}
}
