package com.elhadjium.PMBackend.exception;

public class PMRuntimeException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private int status;

	public PMRuntimeException(String message) {
		super(message);
		status = 500;
	}

	public PMRuntimeException(String message, int status) {
		super(message);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
