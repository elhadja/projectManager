package com.elhadjium.PMBackend.service;

import com.elhadjium.PMBackend.dto.AddUserStoryDTO;
import com.elhadjium.PMBackend.dto.InviteUsersToProjectInputDTO;
import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;

public interface ProjectService {
	public void updateProject(Long projectId, UpdateProjectInputDTO updateProjectInputDTO);
	public void addInvitations(Long projectId, InviteUsersToProjectInputDTO input);
	public long addUserStrotyToBacklog(Long projectId, AddUserStoryDTO userStoryDTO);
	public void addUserStoryToSprint(Long projectId, AddUserStoryDTO userStoryDTO);
	public void deleteUserStoryFromProject(long projectId, long userStoryId);
}