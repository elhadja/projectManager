package com.elhadjium.PMBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elhadjium.PMBackend.dto.ErrorOutputDTO;
import com.elhadjium.PMBackend.dto.LoginInputDTO;
import com.elhadjium.PMBackend.dto.LoginOutputDTO;
import com.elhadjium.PMBackend.dto.signupInputDTO;
import com.elhadjium.PMBackend.entity.CustomUserDetails;
import com.elhadjium.PMBackend.entity.User;
import com.elhadjium.PMBackend.exception.PMRuntimeException;
import com.elhadjium.PMBackend.service.UserService;
import com.elhadjium.PMBackend.util.JwtToken;

@RestController
@RequestMapping("pm-api/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtToken jwt;

	@PostMapping("signup")
	public Long signup(@RequestBody signupInputDTO signupInputDTO) throws Exception {
		signupInputDTO.validate();
		User user = new User();
		user.setEmail(signupInputDTO.getEmail());
		user.setPseudo(signupInputDTO.getPseudo());
		user.setPassword(signupInputDTO.getPassword());

		return userService.signup(user);
	}
	
	@PostMapping("login")
	public ResponseEntity<?> login(@RequestBody LoginInputDTO input) throws Exception {
		input.validate();
		try {
			authManager.authenticate(
				new UsernamePasswordAuthenticationToken(input.getEmail(), input.getPassword())
			); 
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(401)
								 .body(null);
		} 		

		CustomUserDetails user = (CustomUserDetails) userService.loadUserByUsername(input.getEmail());
		UserDetails userDetails = userService.loadUserByUsername(input.getEmail());
		String token = jwt.generateToken(userDetails);

		return ResponseEntity.ok(new LoginOutputDTO(user.getUserId(), token));
	}
	
	@GetMapping("/test")
	public String test() {
		return "test ok";
	}

	// TODO handle Any Exception othan than PMruntimeException
	@ExceptionHandler({PMRuntimeException.class})
	public ResponseEntity<?> handleException(PMRuntimeException ex) {
		ErrorOutputDTO errorOutputDTO = new ErrorOutputDTO();
		errorOutputDTO.setMessage(ex.getMessage());
		errorOutputDTO.setMessageDescription(ex.getMessage());

		return ResponseEntity.status(ex.getStatus()).body(errorOutputDTO);
	}
}