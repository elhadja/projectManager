package com.elhadjium.PMBackend.dto;

import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.util.JavaUtil;

public class AddSprintToProjectInputDTO implements DTOValidator {
	private String name;
	private String startDate;
	private String endDate;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public void validate() {
		if (JavaUtil.isNullOrEmpty(name)) {
			throw new PMInvalidInputDTO("Sprint name are required");
		}
	}
	
}
