package com.elhadjium.PMBackend.integrationTests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.controller.ProjectController;
import com.elhadjium.PMBackend.dao.SprintDAO;
import com.elhadjium.PMBackend.dao.UserStoryDAO;
import com.elhadjium.PMBackend.dto.AddSprintToProjectInputDTO;
import com.elhadjium.PMBackend.dto.AddUserStoryDTO;
import com.elhadjium.PMBackend.dto.GetUserStoryOutputDTO;
import com.elhadjium.PMBackend.entity.Sprint;
import com.elhadjium.PMBackend.entity.UserAccount;
import com.elhadjium.PMBackend.entity.UserStory;
import com.elhadjium.PMBackend.service.ProjectService;
import com.elhadjium.PMBackend.service.UserService;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProjectControllerITest {
	@Autowired
	private ProjectController projectController;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProjectService projectService;

	@Autowired
	private SprintDAO sprintDAO;
	
	@Autowired
	private UserStoryDAO userStoryDAO;

	@Test
	public void getBacklogUserStories_shouldBeOk() throws Exception {
		// prepare
		Long userId = userService.signup(new UserAccount(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		AddUserStoryDTO dto = new AddUserStoryDTO();
		dto.setSummary("summary");
		dto.setDescription("descritpion");
		projectService.addUserStrotyToBacklog(projectId, dto);

		// when
		List<GetUserStoryOutputDTO> userStories = projectController.getBacklogUserStorires(String.valueOf(projectId));
		
		// then
		assertNotNull(userStories);
		assertEquals(1, userStories.size());
		assertEquals(dto.getSummary(), userStories.get(0).getSummary());
		assertEquals(dto.getDescription(), userStories.get(0).getDescription());
	}
	
	@Test
	public void getSprintUserStories_shouldBeOk() throws Exception {
		// prepare
		Long userId = userService.signup(new UserAccount(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		Sprint sprintData = new Sprint();
		sprintData.setName("sprint name");
		Long sprintId = projectService.addSprintToProject(projectId, sprintData);
		
		projectService.addUserStoryToSprint(sprintId, new AddUserStoryDTO("a summary x"));
		projectService.addUserStoryToSprint(sprintId, new AddUserStoryDTO("a summary y"));
		projectService.addUserStoryToSprint(sprintId, new AddUserStoryDTO("a summary z"));

		// when
		List<GetUserStoryOutputDTO> userStories = projectController.getSprintUserStorires(String.valueOf(projectId), String.valueOf(sprintId));
		
		// then
		assertNotNull(userStories);
		assertEquals(3, userStories.size());
	}
	
	@Test
	public void addSprintToProject_shoulbeOk() throws Exception {
		// prepare
		Long userId = userService.signup(new UserAccount(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		AddSprintToProjectInputDTO dto = new AddSprintToProjectInputDTO();
		dto.setName("sprint name");
		
		// when
		Long sprintId = projectController.addSprintToProject(String.valueOf(projectId), dto);
		
		// then
		Sprint sprint = sprintDAO.findById(sprintId).get();
		assertTrue(sprintId > 0);
		assertNotNull(sprint);
		assertEquals(dto.getName(), sprint.getName());
	}
	
	@Test
	public void moveUserStoryFromSprintToBacklog() throws Exception {
		// prepare
		Long userId = userService.signup(new UserAccount(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		Sprint sprintData = new Sprint();
		sprintData.setName("sprint name");
		Long sprintId = projectService.addSprintToProject(projectId, sprintData);
		
		long usId = projectService.addUserStoryToSprint(sprintId, new AddUserStoryDTO("a summary x"));
		
		// when
		projectController.moveUserStoryFromSprintToBacklog(String.valueOf(projectId), String.valueOf(usId));

		// then
		UserStory usToCheck = userStoryDAO.findById(usId).get();
		assertNotNull(usToCheck);
		assertNotNull(usToCheck.getBacklog());
		assertNull(usToCheck.getSprint());
		assertEquals(usId, usToCheck.getId());
	}
}
