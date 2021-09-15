package com.elhadjium.PMBackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PMInvalidInputDTO extends PMRuntimeException {
	public PMInvalidInputDTO(String message) {
		super(message, 400);
	}
}
