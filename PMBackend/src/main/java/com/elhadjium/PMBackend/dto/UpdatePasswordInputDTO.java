package com.elhadjium.PMBackend.dto;

import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;

public class UpdatePasswordInputDTO implements DTOValidator {
	private String password;
	private String confirmedPassword;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmedPassword() {
		return confirmedPassword;
	}

	public void setConfirmedPassword(String confirmedPassword) {
		this.confirmedPassword = confirmedPassword;
	}

	@Override
	public void validate() {
		if (password == null || password.length() == 0 || !password.equals(confirmedPassword)) {
			throw new PMInvalidInputDTO(null, "Passwords must be the same");
		}
	}

}
