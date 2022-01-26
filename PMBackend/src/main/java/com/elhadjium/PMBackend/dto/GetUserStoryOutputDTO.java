package com.elhadjium.PMBackend.dto;

import com.elhadjium.PMBackend.entity.UserStoryImportance;
import com.elhadjium.PMBackend.entity.UserStoryStatus;

public class GetUserStoryOutputDTO {
	private Long id;
	private String summary;
	private String description;
	private Long storyPoint;
	private UserStoryStatus status;
	private UserStoryImportance importance;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public UserStoryStatus getStatus() {
		return status;
	}

	public void setStatus(UserStoryStatus status) {
		this.status = status;
	}

	public UserStoryImportance getImportance() {
		return importance;
	}

	public void setImportance(UserStoryImportance importance) {
		this.importance = importance;
	}
}