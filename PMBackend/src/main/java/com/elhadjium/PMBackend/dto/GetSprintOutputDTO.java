package com.elhadjium.PMBackend.dto;

import java.util.List;

public class GetSprintOutputDTO {
	private long id;
	private String name;
	private String startDate;
	private String endDate;
	private List<GetUserStoryOutputDTO> userStories;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<GetUserStoryOutputDTO> getUserStories() {
		return userStories;
	}

	public void setUserStories(List<GetUserStoryOutputDTO> userStories) {
		this.userStories = userStories;
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
}
