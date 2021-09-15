package com.elhadjium.PMBackend.unitTests.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.entity.User;
import com.elhadjium.PMBackend.exception.PMEntityExistsException;
import com.elhadjium.PMBackend.service.UserService;
import com.elhadjium.PMBackend.service.UserServiceImpl;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
	@Mock
	private UserDAO userDAO;
	
	@InjectMocks
	private UserService userService = new UserServiceImpl();
	
	@Test
	public void signup_ok() throws Exception {
		// prepare
		when(userDAO.save(any(User.class))).thenReturn(new User());

		// When
		userService.signup(new User());
		
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
}
