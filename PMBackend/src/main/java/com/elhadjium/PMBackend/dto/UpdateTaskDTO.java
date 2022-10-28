package com.elhadjium.PMBackend.dto;

import java.util.List;

import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.util.JavaUtil;

public class UpdateTaskDTO implements DTOValidator {
	private Long userId;
	private List<Long> userStoriesIDs;
	private String description;
	private float duration;
	private String definitionOfDone;
	private List<Long> dependenciesIDs;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Long> getUserStoriesIDs() {
		return userStoriesIDs;
	}

	public void setUserStoriesIDs(List<Long> userStoriesIDs) {
		this.userStoriesIDs = userStoriesIDs;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public String getDefinitionOfDone() {
		return definitionOfDone;
	}

	public void setDefinitionOfDone(String definitionOfDone) {
		this.definitionOfDone = definitionOfDone;
	}
	
	public List<Long> getDependenciesIDs() {
		return dependenciesIDs;
	}

	public void setDependenciesIDs(List<Long> dependenciesIDs) {
		this.dependenciesIDs = dependenciesIDs;
	}

	@Override
	public void validate() {
		if(JavaUtil.isNullOrEmpty(description)) {
			throw new PMInvalidInputDTO(null, "task description are required");
		}
		
		try {
			float f = Float.valueOf(duration);
			if (f < 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new PMInvalidInputDTO(null, "Task duration is invalid.");
		}
		
		if (userId <= 0) {
			throw new PMInvalidInputDTO(null, "user identifer must be greather than 0");
		}
	}
}
