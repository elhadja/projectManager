package com.elhadjium.PMBackend.exception;

public class PMRuntimeException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private String details;
	private int statusCode = 500;

	public PMRuntimeException(String message) {
		super(message);
		this.details = message;
	}
	
	protected PMRuntimeException(String message, int statusCode) {
		super(message);
		this.statusCode = statusCode;
	}
	
	protected PMRuntimeException(String message, String details, int statusCode) {
		super(message);
		this.statusCode = statusCode;
		this.details = details;
	}


	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public int getStatusCode() {
		return statusCode;
	}

	protected void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
