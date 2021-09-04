package com.elhadjium.PMBackend.unitTests.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.elhadjium.PMBackend.controller.UserController;
import com.elhadjium.PMBackend.dto.signupInputDTO;
import com.elhadjium.PMBackend.entity.User;
import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.service.UserService;

@ExtendWith(SpringExtension.class)
public class UserControllerTest {
	@Mock
	private UserService userService;
	
	@InjectMocks
	private UserController userController;
	
	@Test
	public void signup_ok() throws Exception {
		// Prepare
		signupInputDTO signupInputDTO = new signupInputDTO();
		signupInputDTO.setEmail("user@test.com");
		signupInputDTO.setPseudo("mockito");
		signupInputDTO.setPassword("trickyPassword#!66664");

		when(userService.signup(any(User.class))).thenReturn(1L);
		
		// When // Then
		assertEquals(1L, userController.signup(signupInputDTO));
	}
	
	@Test
	public void signup_invalidInput() throws Exception {
		// Prepare
		signupInputDTO signupInputDTO = new signupInputDTO();
	
		when(userService.signup(any(User.class))).thenReturn(1L);
		
		try {
			// When
			userController.signup(signupInputDTO);
		} catch (PMInvalidInputDTO e) {
			verify(userService, never()).signup(any());
		}
	}
}