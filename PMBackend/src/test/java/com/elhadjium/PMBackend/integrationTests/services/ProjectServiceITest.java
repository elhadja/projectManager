package com.elhadjium.PMBackend.integrationTests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.controller.ProjectController;
import com.elhadjium.PMBackend.dao.ProjectDAO;
import com.elhadjium.PMBackend.dao.TaskDAO;
import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.dao.UserStoryDAO;
import com.elhadjium.PMBackend.dto.AddUserStoryDTO;
import com.elhadjium.PMBackend.dto.InviteUsersToProjectInputDTO;
import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;
import com.elhadjium.PMBackend.entity.InvitationToProject;
import com.elhadjium.PMBackend.entity.Sprint;
import com.elhadjium.PMBackend.entity.Task;
import com.elhadjium.PMBackend.entity.User;
import com.elhadjium.PMBackend.entity.UserStory;
import com.elhadjium.PMBackend.service.ProjectService;
import com.elhadjium.PMBackend.service.UserService;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProjectServiceITest {
	@Autowired
	ProjectService projectService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserDAO testDAO;
	
	@Autowired
	ProjectDAO projectDAO;
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	UserStoryDAO userStoryDAO;
	
	@Autowired
	TaskDAO taskDAO;
	
	@Test
	@Sql("/data.sql")
	public void test() throws Exception {
	}
	
	@Test
	@Sql("/data.sql")
	@Transactional
	public void updateProject_shouldRemoveSomeUsersAndManagers() throws Exception {
		// prepare
		UpdateProjectInputDTO input = new UpdateProjectInputDTO();
		input.setProjectName("new name");
		input.setProjectManagersIds(List.of(1L));
		input.setProjectUsersIds(List.of(1L, 2L));
		// when
		projectService.updateProject(1L, input);
		
		// then
		Project project = projectDAO.findById(1L).get();
		assertEquals(2, project.getUsers().size());
		assertEquals(1, project.getManagers().size());
	}
	
	@Test
	@Sql("/addInvitationOk.sql")
	@Transactional
	public void addInvitations_ok() throws Exception {
		// prepare
		final long projectId = 1;
		final long expectedNumberOfInvitations = 1;
		final long guestId = 2;

		InviteUsersToProjectInputDTO input = new InviteUsersToProjectInputDTO(guestId, 1L);

		// when
		projectService.addInvitations(projectId, input);
		
		// then
		List<InvitationToProject> invitations = userDAO.findById(guestId).get().getInvitationnToProject();
		assertEquals(expectedNumberOfInvitations, invitations.size());
		assertEquals(projectId, invitations.get(0).getProject().getId());
	}
	
	@Test
	@Transactional
	public void addUserStoryToBacklog_ok() throws Exception {
		// prepare
		Long userId = userService.signup(new User(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		AddUserStoryDTO input = new AddUserStoryDTO();
		input.setDescription("description");
		input.setSummary("summary");
		
		// when
		projectService.addUserStrotyToBacklog(projectId, input);
		
		// then
		Project proejectTest = projectDAO.findById(projectId).get();
		assertNotNull(proejectTest.getBacklog().getUserStories());
		assertEquals(1, project.getBacklog().getUserStories().size());
	}
	
	@Test
	public void deleteUserStoryFromProject_ok() throws Exception {
		// prepare
		Long userId = userService.signup(new User(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		long usId = projectService.addUserStrotyToBacklog(projectId, new AddUserStoryDTO("a summary"));
		
		// when
		projectService.deleteUserStoryFromProject(projectId, usId);
		
		// then
		Project projectAssert = projectDAO.findById(projectId).get();
		assertTrue(projectAssert.getBacklog().getUserStories().isEmpty());
	}
	
	@Test
	public void updateUserStory_shouldBeOk() throws Exception {
		// prepare
		Long userId = userService.signup(new User(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		long usId = projectService.addUserStrotyToBacklog(projectId, new AddUserStoryDTO("a summary"));
		
		UserStory input = new UserStory();
		input.setSummary("new summary");
		input.setDescription("new description");
		input.setStoryPoint(6L);
		
		// when
		projectService.updateUserStory(projectId, usId, input);
		
		// then
		UserStory us = userStoryDAO.findById(usId).get();
		assertNotNull(us);
		assertTrue(input.getSummary().equals(us.getSummary()));
		assertTrue(input.getDescription().equals(us.getDescription()));
		assertTrue(input.getStoryPoint().equals(us.getStoryPoint()));
	}
	
	@Test
	public void moveUserStoryFromSprintToBacklog() throws Exception {
		// prepare
		Long userId = userService.signup(new User(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		Sprint sprintData = new Sprint();
		sprintData.setName("sprint name");
		Long sprintId = projectService.addSprintToProject(projectId, sprintData);
		
		long usId = projectService.addUserStrotyToBacklog(projectId, new AddUserStoryDTO("a summary x"));
		
		// when
		projectService.moveUserStoryToSprint(projectId, sprintId, usId);

		// then
		UserStory usToCheck = userStoryDAO.findById(usId).get();
		assertNotNull(usToCheck);
		assertNotNull(usToCheck.getSprint());
		assertNull(usToCheck.getBacklog());
		assertEquals(usId, usToCheck.getId());
	}
	
	@Test
	public void addTask_shouldBeOk() throws Exception {
		// prepare
		Long userId = userService.signup(new User(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		long usId = projectService.addUserStrotyToBacklog(projectId, new AddUserStoryDTO("a summary"));
		
		Task taskData = new Task();
		taskData.setDescription("desc");
	
		// when
		long taskId = projectService.createTask(usId, taskData);
		
		// then
		assertTrue(taskId > 0);
		assertNotNull(taskDAO.findById(taskId));
	}
	
	@Test
	public void deleteTask_shouldBeOk() throws Exception {
		// prepare
		Long userId = userService.signup(new User(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		long usId = projectService.addUserStrotyToBacklog(projectId, new AddUserStoryDTO("a summary"));
		
		Task taskData = new Task();
		taskData.setDescription("desc");
		
		long taskId = projectService.createTask(usId, taskData);
		
		// when
		projectService.removeTask(usId, taskId);
		
		// then
		assertTrue(taskDAO.findById(taskId).isEmpty());
	}
	
	@Test
	public void getSprintTasks_shouldBeOk() throws Exception {
		// prepare
		Long userId = userService.signup(new User(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		Sprint sprintData = new Sprint();
		sprintData.setName("sprint name");
		Long sprintId = projectService.addSprintToProject(projectId, sprintData);

		long usId = projectService.addUserStoryToSprint(sprintId, new AddUserStoryDTO("a summary 1"));
		
		Task taskData1 = new Task();
		taskData1.setDescription("desc");
		long taskId1 = projectService.createTask(usId, taskData1);
		
		Task taskData2 = new Task();
		taskData2.setDescription("desc 2");
		long taskId2 = projectService.createTask(usId, taskData2);
		
		// when
		Set<Task> tasks = projectService.getSprintTasks(sprintId);
		
		// then
		assertNotNull(tasks);
		assertEquals(2, tasks.size());
	}
}