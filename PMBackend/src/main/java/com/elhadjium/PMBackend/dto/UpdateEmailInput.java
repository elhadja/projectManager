package com.elhadjium.PMBackend.dto;

import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.util.JavaUtil;

public class UpdateEmailInput implements DTOValidator {
	private String email;
	private String url;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void validate() {
		if (JavaUtil.isNullOrEmpty(email) || JavaUtil.isNullOrEmpty(url)) {
			throw new PMInvalidInputDTO(null, "user email and URI are required");
		}
	}
}
