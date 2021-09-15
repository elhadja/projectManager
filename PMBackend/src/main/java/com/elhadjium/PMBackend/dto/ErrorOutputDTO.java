package com.elhadjium.PMBackend.dto;

public class ErrorOutputDTO {
	private String message;
	private String messageDescription;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageDescription() {
		return messageDescription;
	}

	public void setMessageDescription(String messageDescription) {
		this.messageDescription = messageDescription;
	}
}
