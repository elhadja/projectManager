package com.elhadjium.PMBackend.dto;

import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.util.JavaUtil;

public class signupInputDTO implements DTOValidator {
	private String email;
	private String pseudo;
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void validate() {
		if (JavaUtil.isNullOrEmpty(email) || JavaUtil.isNullOrEmpty(pseudo) || JavaUtil.isNullOrEmpty(password)) {
			throw new PMInvalidInputDTO("données invalide");
		}
		// TODO check email format using regex
		// TODO check password format using regex
	}
}
