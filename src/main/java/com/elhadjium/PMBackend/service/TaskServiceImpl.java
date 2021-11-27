package com.elhadjium.PMBackend.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.elhadjium.PMBackend.entity.Task;
import com.elhadjium.PMBackend.entity.UserStoryStatus;
import com.elhadjium.PMBackend.entity.UserStoryTasK;

@Service
public class TaskServiceImpl implements TaskService {

	@Override
	public void openTaskUserStories(Task task) {
		task.getTaskUserStories()
			  .stream()
			  .map(UserStoryTasK::getUserStory)
			  .collect(Collectors.toList())
			  .forEach(us -> {
				us.setStatus(UserStoryStatus.OPEN);
			  });
	}
}
