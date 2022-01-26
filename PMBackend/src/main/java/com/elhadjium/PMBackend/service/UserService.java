package com.elhadjium.PMBackend.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.dto.GetUsersByCriteriaInputDTO;
import com.elhadjium.PMBackend.entity.InvitationToProject;
import com.elhadjium.PMBackend.entity.User;

public interface UserService extends UserDetailsService {
	public Long signup(User user);
	public Long CreateUserProject(Long userId, Project project);
	public List<Project> getUserProjects(Long userId);
	public List<User> getUsersByCriteria(GetUsersByCriteriaInputDTO input);
	public void acceptInvitationToProjects(String[] invitationsIds, Long userId);
	public List<InvitationToProject> getUserInvitationToProject(long guestId);
	public void cancelInvitationToProjects(String[] invitationsIds, Long valueOf);
}
