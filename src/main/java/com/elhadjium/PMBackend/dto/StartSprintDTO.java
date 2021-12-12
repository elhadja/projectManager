package com.elhadjium.PMBackend.dto;

import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;

public class StartSprintDTO implements DTOValidator {
	private String startDate;
	private String endDate;

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		if (startDate != null) {
			this.startDate = startDate.replace("T", " ").replace("Z", "");
		}
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		if (endDate != null) {
			this.endDate = endDate.replace("T", " ").replace("Z", "");
		}
	}

	@Override
	public void validate() {
		if (getStartDate() == null || getEndDate() == null) {
			throw new PMInvalidInputDTO("start date and due date are required");
		}
		
		if (startDate != null && endDate != null && startDate.equals(endDate)) {
			throw new PMInvalidInputDTO("start date should be different with due date");
		}
	}
}
