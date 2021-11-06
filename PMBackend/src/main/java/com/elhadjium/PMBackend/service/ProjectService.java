package com.elhadjium.PMBackend.service;

import java.util.List;
import java.util.Set;

import com.elhadjium.PMBackend.dto.AddUserStoryDTO;
import com.elhadjium.PMBackend.dto.InviteUsersToProjectInputDTO;
import com.elhadjium.PMBackend.dto.StartSprintDTO;
import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;
import com.elhadjium.PMBackend.entity.Sprint;
import com.elhadjium.PMBackend.entity.SprintStatus;
import com.elhadjium.PMBackend.entity.Task;
import com.elhadjium.PMBackend.entity.UserStory;

public interface ProjectService {
	public void updateProject(Long projectId, UpdateProjectInputDTO updateProjectInputDTO);
	public void addInvitations(Long projectId, InviteUsersToProjectInputDTO input);
	public long addUserStrotyToBacklog(Long projectId, AddUserStoryDTO userStoryDTO);
	public long addUserStoryToSprint(Long projectId, AddUserStoryDTO userStoryDTO);
	public void deleteUserStoryFromProject(long projectId, long userStoryId);
	public void updateUserStory(Long projectId, Long userStoryId, UserStory userStoryData);
	public List<UserStory> getBacklogUserStories(Long projectId);
	public List<UserStory> getSprintUserStories(Long parseId, Long parseId2);
	public Long addSprintToProject(Long projectId, Sprint sprintData);
	public void moveUserStoryToBacklog(Long projectId, Long userStoryId);
	public void moveUserStoryToSprint(Long projectId, Long sprintId, Long userStoryId);
	public Long createTask(Long userStoryId, Task taskData);
	public void removeTask(Long userStoryId, Long taskId);
	public Set<Task> getSprintTasks(Long sprintId);
	public List<Sprint> getProjectSprints(Long projectId);
	public void deleteSprint(long projectId, long sprintId);
	public void startSprint(Long projectId, Long sprintId, StartSprintDTO input);
	public void terminateSprint(Long projectId, Long sprintId);
	public void closeUserStory(Long projectId, Long userStoryId);
	public void openUserStory(Long projectId, Long userStoryId);
}