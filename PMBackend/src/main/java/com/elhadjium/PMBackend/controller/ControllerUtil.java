package com.elhadjium.PMBackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.elhadjium.PMBackend.dto.ErrorOutputDTO;
import com.elhadjium.PMBackend.exception.PMRuntimeException;

public class ControllerUtil {
	@ExceptionHandler({Exception.class})
	public static ResponseEntity<?> handleException(Exception ex) {
		if (ex instanceof PMRuntimeException) {
			return handlePMRuntimeExcetionException((PMRuntimeException)ex);
		} 

		ErrorOutputDTO errorOutputDTO = new ErrorOutputDTO();
		errorOutputDTO.setMessageDescription(ex.getMessage());
		errorOutputDTO.setStatusCode(500);

		return ResponseEntity.status(500).body(errorOutputDTO);
	}
	
	private static ResponseEntity<?> handlePMRuntimeExcetionException(PMRuntimeException ex) {
		ErrorOutputDTO errorOutputDTO = new ErrorOutputDTO();
		errorOutputDTO.setMessage(ex.getMessage());
		errorOutputDTO.setMessageDescription(ex.getDetails());
		errorOutputDTO.setStatusCode(ex.getStatusCode());

		return ResponseEntity.status(500).body(errorOutputDTO);
	}

}
