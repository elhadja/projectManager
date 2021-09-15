package com.elhadjium.PMBackend.unitTests.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.elhadjium.PMBackend.controller.UserController;
import com.elhadjium.PMBackend.dto.LoginInputDTO;
import com.elhadjium.PMBackend.dto.signupInputDTO;
import com.elhadjium.PMBackend.entity.CustomUserDetailsImpl;
import com.elhadjium.PMBackend.entity.User;
import com.elhadjium.PMBackend.exception.PMBadCredentialsException;
import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.service.UserService;
import com.elhadjium.PMBackend.util.JwtToken;

@ExtendWith(SpringExtension.class)
public class UserControllerTest {
	@Mock
	private UserService userService;
	
	@Mock
	private AuthenticationManager authManager;
	
	@Mock
	private JwtToken jwt;
	
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
	
	@Test
	public void login_ok() throws Exception {
		//prepare
		LoginInputDTO input = new LoginInputDTO();
		input.setEmail("spring@test.com");
		input.setPassword("mqlkdfjqmlsdkfjqmslkdjfmqlksd");
		UserDetails customUserDetails = new CustomUserDetailsImpl();
		
		when(userService.loadUserByUsername(any())).thenReturn(customUserDetails);
		
		//when 
		assertNotNull(userController.login(input));
	}
	
	@Test
	public void login_invalidInput() throws Exception {
		try {
			// when
			userController.login(new LoginInputDTO());
			fail();
		} catch (PMInvalidInputDTO e) {
			// then
		}
	}
	
	@Test
	public void login_badCredentials() throws Exception {
		// prepare
		LoginInputDTO input = new LoginInputDTO();
		input.setEmail("spring@test.com");
		input.setPassword("mqlkdfjqmlsdkfjqmslkdjfmqlksd");
		
		when(authManager.authenticate(any())).thenThrow(BadCredentialsException.class);
		
		try {
			// when
			userController.login(input);
			fail();
		} catch (PMBadCredentialsException e) {
			// then
		}
	}
}