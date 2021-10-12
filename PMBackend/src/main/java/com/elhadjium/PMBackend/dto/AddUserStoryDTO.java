package com.elhadjium.PMBackend.dto;

import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.util.JavaUtil;

public class AddUserStoryDTO implements DTOValidator{
	private String summary;
	private String description;
	private Long storypoint;

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

	public Long getStorypoint() {
		return storypoint;
	}

	public void setStorypoint(Long storypoint) {
		this.storypoint = storypoint;
	}

	@Override
	public void validate() {
		if (JavaUtil.isNullOrEmpty(summary)) {
			throw new PMInvalidInputDTO("Summary field are required");
		}
		
	}
}
