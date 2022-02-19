package com.elhadjium.PMBackend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.UserProject;
import com.elhadjium.PMBackend.common.PMConstants;
import com.elhadjium.PMBackend.dto.ErrorOutputDTO;
import com.elhadjium.PMBackend.dto.GetUserInvitationsOutputDTO;
import com.elhadjium.PMBackend.dto.GetUserProjectOutputDTO;
import com.elhadjium.PMBackend.dto.GetUsersByCriteriaInputDTO;
import com.elhadjium.PMBackend.dto.GetUsersByCriteriaOutputDTO;
import com.elhadjium.PMBackend.dto.LoginInputDTO;
import com.elhadjium.PMBackend.dto.LoginOutputDTO;
import com.elhadjium.PMBackend.dto.ProjectManagerOutputDTO;
import com.elhadjium.PMBackend.dto.signupInputDTO;
import com.elhadjium.PMBackend.entity.CustomUserDetails;
import com.elhadjium.PMBackend.entity.InvitationToProject;
import com.elhadjium.PMBackend.entity.UserAccount;
import com.elhadjium.PMBackend.exception.PMBadCredentialsException;
import com.elhadjium.PMBackend.exception.PMRuntimeException;
import com.elhadjium.PMBackend.service.UserService;
import com.elhadjium.PMBackend.util.JwtToken;

@RestController
@RequestMapping(PMConstants.PMBaseUri + "/users")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtToken jwt;
	
	@Autowired
	private MessageSource messageSource;

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
			throw new PMBadCredentialsException("Incorrect credentials");
		}

		CustomUserDetails userCusDetails = (CustomUserDetails) userService.loadUserByUsername(input.getUserIdentifier());
		UserDetails userDetails = userService.loadUserByUsername(input.getUserIdentifier());
		String token = jwt.generateToken(userDetails);

		return new LoginOutputDTO(userCusDetails.getUserId(), token);
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
	public List<GetUsersByCriteriaOutputDTO> getUsersByCriteria(@RequestBody GetUsersByCriteriaInputDTO input) throws Exception {
		List<GetUsersByCriteriaOutputDTO> userListOutput = new ArrayList<GetUsersByCriteriaOutputDTO>();
		List<UserAccount> users = userService.getUsersByCriteria(input);
		for (UserAccount user: users) {
			userListOutput.add(new GetUsersByCriteriaOutputDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getPseudo()));
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
	
	@GetMapping("test")
	public String test() {
		return messageSource.getMessage("good.morning.message", null, LocaleContextHolder.getLocale());
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