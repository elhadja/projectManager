package com.elhadjium.PMBackend.unitTests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.elhadjium.PMBackend.entity.Task;
import com.elhadjium.PMBackend.entity.UserStory;
import com.elhadjium.PMBackend.entity.UserStoryStatus;
import com.elhadjium.PMBackend.service.TaskService;
import com.elhadjium.PMBackend.service.TaskServiceImpl;

@ExtendWith(SpringExtension.class)
public class TaskServiceUTest {
	private TaskService taskService = new TaskServiceImpl();
	
	@Test
	public void openTaskUserStories() throws Exception {
		// prepare
		UserStory us1 = new UserStory();
		UserStory us2 = new UserStory();

		us1.setId(1L);
		us1.setStatus(UserStoryStatus.CLOSE);
		us2.setId(2L);
		us2.setStatus(UserStoryStatus.CLOSE);

		Task task = new Task();
		task.addUserStory(us1);
		task.addUserStory(us2);
		
		// when
		taskService.openTaskUserStories(task);
		
		// then
		assertEquals(UserStoryStatus.OPEN, us1.getStatus());
		assertEquals(UserStoryStatus.OPEN, us2.getStatus());
	}
}
