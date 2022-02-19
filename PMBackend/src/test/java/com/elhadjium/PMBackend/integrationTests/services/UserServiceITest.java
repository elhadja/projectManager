package com.elhadjium.PMBackend.integrationTests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.dao.ProjectDAO;
import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.dto.GetUsersByCriteriaInputDTO;
import com.elhadjium.PMBackend.entity.UserAccount;
import com.elhadjium.PMBackend.service.UserService;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Tag("IntegrationTests")
public class UserServiceITest {
	@Autowired
	UserService userService;
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	ProjectDAO projectDAO;

	@Test
	@DisplayName("createUserProject should: create a project + add the user to the project + set the user as manager")
	@Transactional
	public void createUserProject_ok() throws Exception {
		// prepare
		UserAccount user = new UserAccount();
		user.setEmail("email");
		user.setPassword("password");
		user.setPseudo("pseudo");
		userService.signup(user);
		
		Project projectToAdd = new Project();
		projectToAdd.setName("name");

		//when
		userService.CreateUserProject(user.getId(), projectToAdd);
		
		//then
		user = userDAO.findById(user.getId()).get();
		assertEquals(1, user.getProjects().size());
		assertEquals(projectToAdd.getName(), user.getProjects().get(0).getProject().getName());

		assertEquals(1, user.getManagedProjects().size());
		assertEquals(user.getManagedProjects().get(0).getName(), projectToAdd.getName());
	}
	
	@Test
	@Sql("/acceptInvitationToProjectOk.sql")
	@Transactional
	public void acceptInvitationToProject_ok() throws Exception {
		// when
		userService.acceptInvitationToProjects(new String[] {"1"}, 1L);
		
		// then
		UserAccount invitedUser = userDAO.findById(2L).get();
		assertEquals(0, invitedUser.getInvitationnToProject().size());
		assertEquals(1, invitedUser.getProjects().size());
	}
	
	@Test
	@Sql("/acceptInvitationToProjectOk.sql")
	@Transactional
	public void cancelInvitation_ok() throws Exception {
		// when
		userService.cancelInvitationToProjects(new String[] {"1"}, 1L);
		
		// then
		Project project = projectDAO.findById(1L).get();
		UserAccount user = userDAO.findById(2L).get();
		assertEquals(0, project.getInvitationsToProject().size());
		assertEquals(0, user.getInvitationnToProject().size());
	}
	
	@Test
	@Sql("/getUsersByCriteriaOk.sql")
	@Transactional
	public void getUsersByCriteria_shouldFindByFirstNameAndLastName() throws Exception {
		// prepare
		GetUsersByCriteriaInputDTO input = new GetUsersByCriteriaInputDTO();
		input.setPseudo(null);
		input.setFirstname("toto");
		input.setLastname("dupont");

		// when
		List<UserAccount> users = userService.getUsersByCriteria(input);
		
		// then
		assertNotNull(users);
		assertEquals(2, users.size());
	}
	
	@Test
	@Sql("/getUsersByCriteriaOk.sql")
	@Transactional
	public void getUsersByCriteria_shouldFindByPseudoAndFirstNameAndLastName() throws Exception {
		// prepare
		GetUsersByCriteriaInputDTO input = new GetUsersByCriteriaInputDTO();
		input.setPseudo("elhadjx");
		input.setFirstname("toto");
		input.setLastname("dupont");

		// when
		List<UserAccount> users = userService.getUsersByCriteria(input);
		
		// then
		assertNotNull(users);
		assertEquals(1, users.size());
	}

}