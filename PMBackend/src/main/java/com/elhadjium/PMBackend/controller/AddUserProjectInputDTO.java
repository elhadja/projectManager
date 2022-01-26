package com.elhadjium.PMBackend.controller;

import com.elhadjium.PMBackend.dto.DTOValidator;
import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.util.JavaUtil;

public class AddUserProjectInputDTO implements DTOValidator {
	private String name;
	private String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void validate() {
		if (JavaUtil.isNullOrEmpty(name)) {
			// TODO to internationalize
			throw new PMInvalidInputDTO("invalid input");
		}
	}

}
