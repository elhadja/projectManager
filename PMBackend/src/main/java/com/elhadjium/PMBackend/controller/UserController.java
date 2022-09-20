package com.elhadjium.PMBackend.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.UserProject;
import com.elhadjium.PMBackend.common.MessageManager;
import com.elhadjium.PMBackend.common.PMConstants;
import com.elhadjium.PMBackend.controller.constant.UserControllerConstant;
import com.elhadjium.PMBackend.dto.AddUserProjectInputDTO;
import com.elhadjium.PMBackend.dto.ErrorOutputDTO;
import com.elhadjium.PMBackend.dto.GetUserInvitationsOutputDTO;
import com.elhadjium.PMBackend.dto.GetUserProjectOutputDTO;
import com.elhadjium.PMBackend.dto.GetUsersByCriteriaInputDTO;
import com.elhadjium.PMBackend.dto.LoginInputDTO;
import com.elhadjium.PMBackend.dto.LoginOutputDTO;
import com.elhadjium.PMBackend.dto.PasswordReinitialisationTokenInputDTO;
import com.elhadjium.PMBackend.dto.ProjectManagerOutputDTO;
import com.elhadjium.PMBackend.dto.UpdateEmailInput;
import com.elhadjium.PMBackend.dto.UpdatePasswordInputDTO;
import com.elhadjium.PMBackend.dto.UserDTO;
import com.elhadjium.PMBackend.dto.signupInputDTO;
import com.elhadjium.PMBackend.entity.CustomUserDetails;
import com.elhadjium.PMBackend.entity.InvitationToProject;
import com.elhadjium.PMBackend.entity.UserAccount;
import com.elhadjium.PMBackend.exception.PMBadCredentialsException;
import com.elhadjium.PMBackend.exception.PMEntityNotExistsException;
import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.exception.PMRuntimeException;
import com.elhadjium.PMBackend.service.UserService;
import com.elhadjium.PMBackend.util.JwtToken;
import com.google.api.client.auth.openidconnect.IdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import io.jsonwebtoken.ExpiredJwtException;

@RestController
@RequestMapping(PMConstants.PMBaseUri + "/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private MessageManager messageManager;
	
	@Autowired
	private JwtToken jwt;
	
	// TODO to remove
	@PostMapping("testEmail")
	public void sendEmail(@RequestBody String messageText) {
		userService.sendSimpleEmail("elhadja007@gmail.com", "test mail service", "this message is just to test that mail sending are working on project manager webpp");
	}

	@PostMapping("signup")
	public Long signup(@RequestBody signupInputDTO signupInputDTO) throws Exception {
		signupInputDTO.validate();
		UserAccount user = new UserAccount();
		user.setEmail(signupInputDTO.getEmail());
		user.setPseudo(signupInputDTO.getPseudo());
		user.setPassword(signupInputDTO.getPassword());

		return userService.signup(user);
	}
	
	@PostMapping("login")
	public LoginOutputDTO login(@RequestBody LoginInputDTO input) throws Exception {
		input.validate();

		try {
			authManager.authenticate(new UsernamePasswordAuthenticationToken(input.getUserIdentifier(), input.getPassword())); 
		} catch (BadCredentialsException e) {
			throw new PMBadCredentialsException(messageManager.getTranslation(MessageManager.INCORRECT_CREDENTIALS));
		}

		CustomUserDetails userCusDetails = (CustomUserDetails) userService.loadUserByUsername(input.getUserIdentifier());
		UserDetails userDetails = userService.loadUserByUsername(input.getUserIdentifier());
		Long tokenExpiration = jwt.getApiTokenExpiration();
		String token = jwt.generateToken(userDetails.getUsername(), tokenExpiration, JwtToken.SECRET_KEY);

		return new LoginOutputDTO(userCusDetails.getUserId(), token, tokenExpiration);
	}
	
	@PostMapping("loginWithGoogle")
	public LoginOutputDTO loginWithGoogle(@RequestBody String googleTokenId) throws Exception {
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
				.setAudience(Collections.singletonList("834948493456-5qgjhtcvu6v0ueso0nbmed40m4a3r2ev.apps.googleusercontent.com"))
				.build();
		GoogleIdToken googleIdToken = verifier.verify(googleTokenId);
		if (googleIdToken != null) {
			Payload payload = googleIdToken.getPayload();
			String email = (String) payload.get("email");
			UserDetails userDetail = userService.loadUserByUsername(email);
			if (userDetail != null) {
				Long tokenExpiration = jwt.getApiTokenExpiration();
				String token = jwt.generateToken(userDetail.getUsername(), tokenExpiration, JwtToken.SECRET_KEY);
				return new LoginOutputDTO(((CustomUserDetails) userDetail).getUserId(), token, tokenExpiration);
			}
		}
		
		throw new PMBadCredentialsException(messageManager.getTranslation(MessageManager.INCORRECT_CREDENTIALS));
	}
	
	@PostMapping("{id}/projects")
	public Long createUserProject(@RequestBody AddUserProjectInputDTO userProjectInputDTO,
									@PathVariable("id") String userId) throws Exception {
		Project project = new Project();
		project.setName(userProjectInputDTO.getName());
		project.setDescription(userProjectInputDTO.getDescription());
		return userService.CreateUserProject(Long.valueOf(userId), project);
	}
	
	@GetMapping("{id}/projects")
	public List<GetUserProjectOutputDTO> getUserProjects(@PathVariable("id") String userId) throws Exception {
		List<Project> userProjects = userService.getUserProjects(Long.valueOf(userId));
		List<GetUserProjectOutputDTO> getUserProjectOutputDTOs = new ArrayList<GetUserProjectOutputDTO>();
		for (Project project: userProjects) {
			GetUserProjectOutputDTO userProjectOutputDTO = new GetUserProjectOutputDTO();
			userProjectOutputDTO.setProjectDescription(project.getDescription());
			userProjectOutputDTO.setProjectName(project.getName());
			userProjectOutputDTO.setProjectId(project.getId());
			userProjectOutputDTO.setProjectManagers(new ArrayList<ProjectManagerOutputDTO>());
			for (UserAccount user: project.getManagers()) {
				ProjectManagerOutputDTO manager = new ProjectManagerOutputDTO();
				manager.setPseudo(user.getPseudo());
				manager.setId(user.getId());
				userProjectOutputDTO.getProjectManagers().add(manager);
			}
			userProjectOutputDTO.setProjectUsers(new ArrayList<ProjectManagerOutputDTO>());
			for (UserProject user: project.getUsers()) {
				ProjectManagerOutputDTO projectUser = new ProjectManagerOutputDTO();
				projectUser.setPseudo(user.getUser().getPseudo());
				projectUser.setId(user.getUser().getId());
				userProjectOutputDTO.getProjectUsers().add(projectUser);

			}
			
			getUserProjectOutputDTOs.add(userProjectOutputDTO);
		}
		
		return getUserProjectOutputDTOs;
	}
	
	// TODO add limit
	@PostMapping
	public List<UserDTO> getUsersByCriteria(@RequestBody GetUsersByCriteriaInputDTO input) throws Exception {
		List<UserDTO> userListOutput = new ArrayList<UserDTO>();
		List<UserAccount> users = userService.getUsersByCriteria(input);
		for (UserAccount user: users) {
			userListOutput.add(new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getPseudo(), null));
		}
		return userListOutput;
	}
	
	@PostMapping("{id}/projects/{invitationsIds}")
	public void acceptInvitationToProject(@PathVariable("invitationsIds") String[] invitationsIds, @PathVariable("id") String userId) {
		userService.acceptInvitationToProjects(invitationsIds, Long.valueOf(userId));
	}
	
	@DeleteMapping("{id}/projects/{invitationsIds}")
	public void CancelInvitationToProject(@PathVariable("invitationsIds") String[] invitationsIds, @PathVariable("id") String userId) {
		userService.cancelInvitationToProjects(invitationsIds, Long.valueOf(userId));
	}

	
	@GetMapping("{id}/invitationToProjects")
	public List<GetUserInvitationsOutputDTO> getUserInvitations(@PathVariable("id") String id) {
		List<InvitationToProject> inviations = userService.getUserInvitationToProject(Long.valueOf(id));
		List<GetUserInvitationsOutputDTO> result = new ArrayList<GetUserInvitationsOutputDTO>();
		inviations.forEach((InvitationToProject i) -> {
			GetUserInvitationsOutputDTO output = new GetUserInvitationsOutputDTO();
			output.setAuthorPseudo(i.getAuthor().getPseudo());
			output.setProjectDescription(i.getProject().getDescription());
			output.setProjectName(i.getProject().getName());
			output.setInvitationToProjectId(i.getId());
			result.add(output);
		});
		return result;
	}
	
	@PostMapping(UserControllerConstant.passwordReinitialisationToken)
	public void generateTokenForPasswordReinitialisation(@RequestBody PasswordReinitialisationTokenInputDTO input) {
		input.validate();
		StringBuilder link = new StringBuilder(input.getUrl());
		link.append("?token=");
		link.append(jwt.generateToken(input.getEmail(), System.currentTimeMillis() + (12 * 60 * 60 * 1000), "secret"));
		
		userService.sendSimpleEmail(input.getEmail(),
									messageManager.getTranslation(MessageManager.PASSWORD_RESET_SUBJECT),
									link.toString());
	}
	
	private String buildReinitialisationLink(String frontendURI, String tokenId) {
		StringBuilder link = new StringBuilder(frontendURI);
		link.append("?token=");
		link.append(jwt.generateToken(tokenId, System.currentTimeMillis() + (12 * 60 * 60 * 1000), "secret"));
		
		return link.toString();
	}
	
	@PostMapping(UserControllerConstant.reinitializePassword + "/{token}")
	public void reinitializePassword(@RequestBody String newUserPassword, @PathVariable("token") String jwToken) {
		String email = null;
		try {
			email = jwt.extractUsername(jwToken);
			userService.loadUserByUsername(email);
		} catch (ExpiredJwtException | UsernameNotFoundException e) {
			throw new PMInvalidInputDTO(messageManager.getTranslation(MessageManager.INVALLID_TOKEN));
		}

		userService.updateUserPassword(email, newUserPassword);
	}
	
	@GetMapping("{id}")
	public UserDTO getUserById(@PathVariable("id") Long userId) throws Exception {
		UserAccount user = userService.getUserById(userId);
		return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getPseudo(), user.getEmail());
	}
	
	@PutMapping("{id}")
	public void updateUser(@RequestBody UserDTO userInput, @PathVariable("id") String userId) {
		UserAccount user = new UserAccount();
		user.setId(Long.valueOf(userId));
		user.setFirstName(userInput.getFirstname());
		user.setLastName(userInput.getLastname());
		user.setPseudo(userInput.getPseudo());
		user.setEmail(userInput.getEmail());
		userService.updateUser(user);
	}
	
	@PutMapping("{id}/updatePassword")
	public void updateUserPassword(@RequestBody UpdatePasswordInputDTO input, @PathVariable("id") String userId) {
		userService.updateUserPassword(Long.valueOf(userId), input.getPassword());
	}
	
	@PostMapping("{id}/updateEmail")
	public void updateuserEmail(@PathVariable("id") String userId, @RequestBody UpdateEmailInput input) {
		try {
			UserAccount user = userService.getUserById(Long.valueOf(userId));
			userService.sendSimpleEmail(input.getEmail(),
									messageManager.getTranslation(MessageManager.MAIL_CONFIRMATION_SUBJECT),
									buildReinitialisationLink(input.getUrl(), user.getEmail() + "##" + input.getEmail()));

		} catch (NoSuchElementException e) {
			throw new PMEntityNotExistsException(messageManager.getTranslation(MessageManager.ENTITY_NOT_FOUND_ERROR));
		}
	}

	@PostMapping("{id}/confirm-update-email")
	public void confirmUpdateEmail(@PathVariable("id") String userId, @RequestBody String token) {
		String newEmail = null;
		try {
			String mails = jwt.extractUsername(token);
			userService.loadUserByUsername(mails.split("##")[0]);
			newEmail = mails.split("##")[1];
		} catch (ExpiredJwtException | UsernameNotFoundException e) {
			throw new PMInvalidInputDTO(messageManager.getTranslation(MessageManager.INVALLID_TOKEN));
		}

		userService.UpdateUserEmail(Long.valueOf(userId), newEmail);
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