package com.elhadjium.PMBackend.service;

import java.util.List;

import com.elhadjium.PMBackend.dto.AddUserStoryDTO;
import com.elhadjium.PMBackend.dto.InviteUsersToProjectInputDTO;
import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;
import com.elhadjium.PMBackend.entity.Sprint;
import com.elhadjium.PMBackend.entity.UserStory;

public interface ProjectService {
	public void updateProject(Long projectId, UpdateProjectInputDTO updateProjectInputDTO);
	public void addInvitations(Long projectId, InviteUsersToProjectInputDTO input);
	public long addUserStrotyToBacklog(Long projectId, AddUserStoryDTO userStoryDTO);
	public void addUserStoryToSprint(Long projectId, AddUserStoryDTO userStoryDTO);
	public void deleteUserStoryFromProject(long projectId, long userStoryId);
	public void updateUserStory(Long projectId, Long userStoryId, UserStory userStoryData);
	public List<UserStory> getBacklogUserStories(Long projectId);
	public List<UserStory> getSprintUserStories(Long parseId, Long parseId2);
	public Long addSprintToProject(Long projectId, Sprint sprintData);
}