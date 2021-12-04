package com.elhadjium.PMBackend.dto;

import java.util.List;

import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.util.JavaUtil;

public class AddTaskInputDTO implements DTOValidator {
	private Long userId;
	private long userStoryId;
	private String description;
	private Float duration;
	private String definitionOfDone;
	private List<Long> dependenciesIDs;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public long getUserStoryId() {
		return userStoryId;
	}

	public void setUserStoryId(long userStoryId) {
		this.userStoryId = userStoryId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getDuration() {
		return duration;
	}

	public void setDuration(Float duration) {
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
			throw new PMInvalidInputDTO("task description are required");
		}
		
		if (duration != null && duration.floatValue() <= 0) {
			throw new PMInvalidInputDTO("Task duration must be greather than 0.");
		}
		
		if ((userId != null && userId.longValue() <=  0) || userStoryId <= 0) {
			throw new PMInvalidInputDTO("user and userStory identifers must be greather than 0");
		}
	}
}
