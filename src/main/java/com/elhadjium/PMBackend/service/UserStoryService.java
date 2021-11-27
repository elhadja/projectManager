package com.elhadjium.PMBackend.service;

import com.elhadjium.PMBackend.entity.UserStory;

public interface UserStoryService {
	boolean isAllUserStoryTasksAreDone(UserStory userStory);
}
