package com.elhadjium.PMBackend.dto;

import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.util.JavaUtil;

public class LoginInputDTO implements DTOValidator {
	private String userIdentifier;
	private String password;

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String email) {
		this.userIdentifier = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void validate() {
		if (JavaUtil.isNullOrEmpty(userIdentifier)) {
			throw new PMInvalidInputDTO("You should enter an email or an pseudo");
		}
		if (JavaUtil.isNullOrEmpty(password)) {
			throw new PMInvalidInputDTO("Le mot de passe est obligatoire et ne peux pas être vide");
		}
	}
}
