package com.elhadjium.PMBackend.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.dto.GetUsersByCriteriaInputDTO;
import com.elhadjium.PMBackend.dto.UserDTO;
import com.elhadjium.PMBackend.entity.InvitationToProject;
import com.elhadjium.PMBackend.entity.UserAccount;

public interface UserService extends UserDetailsService {
	public Long signup(UserAccount user);
	public Long CreateUserProject(Long userId, Project project);
	public List<Project> getUserProjects(Long userId);
	public List<UserAccount> getUsersByCriteria(GetUsersByCriteriaInputDTO input);
	public void acceptInvitationToProjects(String[] invitationsIds, Long userId);
	public List<InvitationToProject> getUserInvitationToProject(long guestId);
	public void cancelInvitationToProjects(String[] invitationsIds, Long valueOf);
	public void updateUserPassword(String userEmail, String newUserPassword);
	// TODO move this method in a more appropriate service
	public void sendSimpleEmail(String targetEmail, String subject, String content);
	public UserAccount getUserById(Long userId);
	public UserAccount updateUser(UserAccount user);
}
