package com.elhadjium.PMBackend.dto;

import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.util.JavaUtil;

public class AddSprintToProjectInputDTO implements DTOValidator {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void validate() {
		if (JavaUtil.isNullOrEmpty(name)) {
			throw new PMInvalidInputDTO("Sprint name are required");
		}
	}
	
}
