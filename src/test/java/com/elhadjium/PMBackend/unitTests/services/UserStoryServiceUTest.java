package com.elhadjium.PMBackend.unitTests.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.elhadjium.PMBackend.entity.Task;
import com.elhadjium.PMBackend.entity.TaskStatus;
import com.elhadjium.PMBackend.entity.UserStory;
import com.elhadjium.PMBackend.service.UserStoryService;
import com.elhadjium.PMBackend.service.UserStoryServiceImpl;

@ExtendWith(SpringExtension.class)
public class UserStoryServiceUTest {
	private UserStoryService userStoryService = new UserStoryServiceImpl();
	
	@Test
	public void isAllUserStoryTasksAreDone_shouldBeOk() throws Exception {
		// prepare
		Task task1 = new Task();
		task1.setId(2L);
		task1.setStatus(TaskStatus.DONE);

		Task task2 = new Task();
		task2.setId(3L);
		task2.setStatus(TaskStatus.DONE);

		UserStory us = new UserStory();
		us.setId(1L);
		us.addTask(task1);
		us.addTask(task2);
		
		// when 
		boolean result = userStoryService.isAllUserStoryTasksAreDone(us);
		
		// then
		assertTrue(result);
	}
	
	@Test
	public void isAllUserStoryTasksAreDone_shouldNotBeOk() throws Exception {
		// prepare
		Task task1 = new Task();
		task1.setId(2L);
		task1.setStatus(TaskStatus.DOING);

		Task task2 = new Task();
		task2.setId(3L);
		task2.setStatus(TaskStatus.DONE);

		UserStory us = new UserStory();
		us.setId(1L);
		us.addTask(task1);
		us.addTask(task2);
		
		// when 
		boolean result = userStoryService.isAllUserStoryTasksAreDone(us);
		
		// then
		assertFalse(result);
	}

}
