package com.elhadjium.PMBackend.dto;

import com.elhadjium.PMBackend.entity.UserStoryStatus;
import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.util.JavaUtil;

public class AddUserStoryDTO implements DTOValidator{
	private String summary;
	private String description;
	private Long storyPoint;
	private UserStoryStatus status;
	
	public AddUserStoryDTO() {
	}
	
	public AddUserStoryDTO(String summary) {
		this.summary = summary;
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

	public void setStoryPoint(Long storypoint) {
		this.storyPoint = storypoint;
	}

	public UserStoryStatus getStatus() {
		return status;
	}

	public void setStatus(UserStoryStatus status) {
		this.status = status;
	}

	@Override
	public void validate() {
		if (JavaUtil.isNullOrEmpty(summary)) {
			throw new PMInvalidInputDTO("Summary field are required");
		}
		
	}
}
