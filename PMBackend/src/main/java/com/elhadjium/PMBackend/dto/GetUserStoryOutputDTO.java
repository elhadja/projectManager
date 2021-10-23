package com.elhadjium.PMBackend.dto;

public class GetUserStoryOutputDTO {
	private String id;
	private String summary;
	private String description;
	private Long storyPoint;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getStoryPoint() {
		return storyPoint;
	}
	public void setStoryPoint(Long storyPoint) {
		this.storyPoint = storyPoint;
	}
}
