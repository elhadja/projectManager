package com.elhadjium.PMBackend.service;

import org.springframework.stereotype.Service;

import com.elhadjium.PMBackend.entity.TaskStatus;
import com.elhadjium.PMBackend.entity.UserStory;
import com.elhadjium.PMBackend.entity.UserStoryTasK;

@Service
public class UserStoryServiceImpl implements UserStoryService {

	@Override
	public boolean isAllUserStoryTasksAreDone(UserStory userStory) {
		return userStory.getUserStoryTasks()
				  .stream()
				  .map(UserStoryTasK::getTask)
				  .allMatch(userStoryTask -> userStoryTask.getStatus().equals(TaskStatus.DONE));
	}
}
