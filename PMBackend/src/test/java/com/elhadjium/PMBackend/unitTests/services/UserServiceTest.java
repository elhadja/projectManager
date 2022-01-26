package com.elhadjium.PMBackend.unitTests.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.dao.ProjectDAO;
import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.entity.User;
import com.elhadjium.PMBackend.exception.PMEntityExistsException;
import com.elhadjium.PMBackend.service.UserService;
import com.elhadjium.PMBackend.service.UserServiceImpl;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
	@Mock
	private UserDAO userDAO;
	
	@Mock
	private ProjectDAO projectDAO;
	
	@Mock
	private MessageSource messageSource;
	
	@InjectMocks
	private UserService userService = new UserServiceImpl();
	
	@Test
	public void signup_ok() throws Exception {
		
		User user = new User();
		user.setPassword("password");
		// mock
		when(userDAO.save(any(User.class))).thenReturn(new User());

		// When
		userService.signup(user);
		
		// Then
		verify(userDAO).save(any(User.class));
	}
	
	@Test
	public void signup_userAlreadyExists() throws Exception {
		// Prepare
		User user = new User();
		when(userDAO.findByEmail(any())).thenReturn(user);
		when(userDAO.findByPseudo(any())).thenReturn(user);
		
		try {
			// when
			userService.signup(new User());
		} catch (PMEntityExistsException e) {
			
		}
	}
	
	@Test
	public void createUserProject_shouldFailIFTheProjectAlreadyExists() throws Exception {
		// prepare
		Project project = new Project();
		project.setName("xxxx");
		
		// mock
		when(projectDAO.findByName(project.getName())).thenReturn(project);

		try {
			// when
			userService.CreateUserProject(1L, project);
			fail();
		} catch (PMEntityExistsException e) {
			// TODO: handle exception
		}
	}
}
