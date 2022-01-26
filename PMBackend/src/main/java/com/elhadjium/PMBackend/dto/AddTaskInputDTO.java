package com.elhadjium.PMBackend.dto;

import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.util.JavaUtil;

public class AddTaskInputDTO implements DTOValidator {
	private long userId;
	private long userStoryId;
	private String description;
	private float duration;
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
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

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	@Override
	public void validate() {
		if(JavaUtil.isNullOrEmpty(description)) {
			throw new PMInvalidInputDTO("task description are required");
		}
		
		try {
			float f = Float.valueOf(duration);
			if (f < 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new PMInvalidInputDTO("Task duration is invalid.");
		}
		
		if (userId <= 0 || userStoryId <= 0) {
			throw new PMInvalidInputDTO("user and userStory identifers must be greather than 0");
		}
	}
}
