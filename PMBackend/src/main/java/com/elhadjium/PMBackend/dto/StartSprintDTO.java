package com.elhadjium.PMBackend.dto;

public class StartSprintDTO {
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
}
