package com.elhadjium.PMBackend.integrationTests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.controller.ProjectController;
import com.elhadjium.PMBackend.dao.ProjectDAO;
import com.elhadjium.PMBackend.dao.SprintDAO;
import com.elhadjium.PMBackend.dao.TaskDAO;
import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.dao.UserStoryDAO;
import com.elhadjium.PMBackend.dto.AddUserStoryDTO;
import com.elhadjium.PMBackend.dto.InviteUsersToProjectInputDTO;
import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;
import com.elhadjium.PMBackend.entity.CustomUserDetailsImpl;
import com.elhadjium.PMBackend.entity.InvitationToProject;
import com.elhadjium.PMBackend.entity.Sprint;
import com.elhadjium.PMBackend.entity.SprintStatus;
import com.elhadjium.PMBackend.entity.Task;
import com.elhadjium.PMBackend.entity.UserAccount;
import com.elhadjium.PMBackend.entity.UserStory;
import com.elhadjium.PMBackend.entity.UserStoryStatus;
import com.elhadjium.PMBackend.service.ProjectService;
import com.elhadjium.PMBackend.service.UserService;

import javassist.NotFoundException;

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
	
	@Autowired
	SprintDAO sprintDAO;
	
	@BeforeEach
	public void setup() {
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(new  CustomUserDetailsImpl(), null));
	}
	
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
		Long userId = userService.signup(new UserAccount(null, null, null, "email@test.com", "pseudo", "trickypassword"));
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
	public void deleteUserStoryFromProject_shouldRemoveUserStoryFromSprint() throws Exception {
		// prepare
		Long userId = userService.signup(new UserAccount(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		Sprint sprintData = new Sprint();
		sprintData.setName("sprint name");
		Long sprintId = projectService.addSprintToProject(projectId, sprintData);

		
		long usId = projectService.addUserStoryToSprint(sprintId, new AddUserStoryDTO("a summary"));
		
		// when
		projectService.deleteUserStoryFromProject(projectId, usId);
		
		// then
		// user story should not exists in sprint
		List<UserStory> userStories = projectService.getSprintUserStories(projectId, sprintId);
		assertTrue(userStories.isEmpty());
		
		// user story should not exists in database
		try {
			UserStory us = userStoryDAO.findById(usId).get();
			fail();
		} catch (NoSuchElementException e) {
			
		}
	}
	
	@Test
	public void deleteUserStoryFromProject_shouldRemoveUserStoryFromBacklog() throws Exception {
		// prepare
		Long userId = userService.signup(new UserAccount(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		long usId = projectService.addUserStrotyToBacklog(projectId, new AddUserStoryDTO("a summary"));
		
		// when
		projectService.deleteUserStoryFromProject(projectId, usId);
		
		// then
		// us should not exist in backlog
		Project projectAssert = projectDAO.findById(projectId).get();
		assertTrue(projectAssert.getBacklog().getUserStories().isEmpty());
		// us should not exist in database
		try {
			userStoryDAO.findById(usId).get();
			fail();
		} catch (NoSuchElementException e) {
			// TODO: handle exception
		}
	}
	
	@Test
	public void updateUserStory_shouldBeOk() throws Exception {
		// prepare
		Long userId = userService.signup(new UserAccount(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
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
		Long userId = userService.signup(new UserAccount(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
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
	public void createTask_shouldBeOk() throws Exception {
		// prepare
		Long userId = userService.signup(new UserAccount(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		long usId = projectService.addUserStrotyToBacklog(projectId, new AddUserStoryDTO("a summary"));
		
		Task taskData = new Task();
		taskData.setDescription("desc");
		//taskData.setUser();	
		// when
		long taskId = projectService.createTask(taskData);
		
		// then
		assertTrue(taskId > 0);
		assertNotNull(taskDAO.findById(taskId));
	}
	
	@Test
	public void deleteTask_shouldBeOk() throws Exception {
		// prepare
		Long userId = userService.signup(new UserAccount(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		long usId = projectService.addUserStrotyToBacklog(projectId, new AddUserStoryDTO("a summary"));
		
		Task taskData = new Task();
		taskData.setDescription("desc");
		
		long taskId = projectService.createTask(taskData);
		
		// when
		projectService.removeTask(usId, taskId);
		
		// then
		assertTrue(taskDAO.findById(taskId).isEmpty());
	}
	
	@Test
	public void getSprintTasks_shouldBeOk() throws Exception {
		// prepare
		Long userId = userService.signup(new UserAccount(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		Sprint sprintData = new Sprint();
		sprintData.setName("sprint name");
		Long sprintId = projectService.addSprintToProject(projectId, sprintData);

		long usId = projectService.addUserStoryToSprint(sprintId, new AddUserStoryDTO("a summary 1"));
		

		UserStory userStory = new UserStory();
		userStory.setId(usId);
		Task taskData1 = new Task();
		taskData1.setDescription("desc");
		taskData1.addUserStory(userStory);
		long taskId1 = projectService.createTask(taskData1);
		
		Task taskData2 = new Task();
		taskData2.setDescription("desc 2");
		taskData2.addUserStory(userStory);
		long taskId2 = projectService.createTask(taskData2);
		
		// when
		Set<Task> tasks = projectService.getSprintTasks(sprintId);
		
		// then
		assertNotNull(tasks);
		assertEquals(2, tasks.size());
	}
	
	@Test
	public void deleteSprint_shouldMoveAllUserStoriesToBacklogBeforeDeleting() throws Exception {
		// prepare
		Long userId = userService.signup(new UserAccount(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		Sprint sprintData = new Sprint();
		sprintData.setName("sprint name");
		Long sprintId = projectService.addSprintToProject(projectId, sprintData);
		
		long usId = projectService.addUserStoryToSprint(sprintId, new AddUserStoryDTO("a summary 1"));
		
		// when
		projectService.deleteSprint(projectId, sprintId);
		
		// then
		// all sprint's user stories should be moved from sprint to backlog
		Project projectToCheck = projectDAO.findById(projectId).get();
		assertEquals(1, projectToCheck.getBacklog().getUserStories().size());
		
		// sprint should no exists in database
		try {
			sprintDAO.findById(sprintId).get();
			fail();
		} catch (NoSuchElementException e) {
			// TODO: handle exception
		}
	}
	
	@Test
	@Transactional
	public void terminateSprint_shouldBeOk() throws Exception {
		// prepare
		Long userId = userService.signup(new UserAccount(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		Sprint sprintData = new Sprint();
		sprintData.setName("sprint name");
		Long sprintId = projectService.addSprintToProject(projectId, sprintData);
		
		long usId = projectService.addUserStoryToSprint(sprintId, new AddUserStoryDTO("a summary 1"));
		
		// when
		projectService.terminateSprint(projectId, sprintId);
		
		// then
		List<Sprint> list = projectService.getProjectSprints(projectId);
		Sprint sprintToCheck = list.get(0);
		assertEquals(SprintStatus.CLOSED, sprintToCheck.getStatus());
		sprintToCheck.getUserStories().forEach(us -> assertEquals(UserStoryStatus.CLOSED, sprintToCheck.getStatus()));
	}
	
	@Test()
	public void removeUserFromProject_shouldRemoveTheProject() throws Exception {
		// prepare
		Long userId = userService.signup(new UserAccount(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		Long userId2 = userService.signup(new UserAccount(null, null, null, "email2@test.com", "pseudo2", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		UpdateProjectInputDTO input = new UpdateProjectInputDTO();
		input.setProjectUsersIds(List.of(userId, userId2));
		input.setProjectManagersIds(List.of(userId));
		input.setProjectName(project.getName());
		projectService.updateProject(projectId, input);
		
		// when
		projectService.removeUserFromProject(projectId, userId);
		
		// then
		assertTrue(projectDAO.findById(projectId).isEmpty());
	}

	@Test
	@Transactional
	public void removeUserFromProject_shouldRemoveUserFromProject() throws Exception {
		// prepare
		Long userId = userService.signup(new UserAccount(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		Long userId2 = userService.signup(new UserAccount(null, null, null, "email2@test.com", "pseudo2", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		UpdateProjectInputDTO input = new UpdateProjectInputDTO();
		input.setProjectUsersIds(List.of(userId, userId2));
		input.setProjectManagersIds(List.of(userId));
		input.setProjectName(project.getName());
		projectService.updateProject(projectId, input);
		
		// when
		projectService.removeUserFromProject(projectId, userId2);
		
		// then
		UserAccount user = userService.getUserById(userId2);
		assertTrue(user.getProjects().isEmpty());
	}
	
	@Test
	@Transactional
	public void removeUserFromProject_shouldRemoveUserFromProjectAndAsManager() throws Exception {
		// prepare
		Long userId = userService.signup(new UserAccount(null, null, null, "email@test.com", "pseudo", "trickypassword"));
		Long userId2 = userService.signup(new UserAccount(null, null, null, "email2@test.com", "pseudo2", "trickypassword"));
		
		Project project = new Project();
		project.setName("project name");
		Long projectId = userService.CreateUserProject(userId, project);
		
		UpdateProjectInputDTO input = new UpdateProjectInputDTO();
		input.setProjectUsersIds(List.of(userId, userId2));
		input.setProjectManagersIds(List.of(userId, userId2));
		input.setProjectName(project.getName());
		projectService.updateProject(projectId, input);
		
		// when
		projectService.removeUserFromProject(projectId, userId2);
		
		// then
		UserAccount user = userService.getUserById(userId2);
		assertTrue(user.getProjects().isEmpty());
		Project projectUpdated = projectDAO.findById(projectId).get();
		assertTrue(projectUpdated.getManagers().size() == 1);
		
	}
}