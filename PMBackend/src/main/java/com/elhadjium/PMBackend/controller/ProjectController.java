package com.elhadjium.PMBackend.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elhadjium.PMBackend.common.Mapping;
import com.elhadjium.PMBackend.common.PMConstants;
import com.elhadjium.PMBackend.dto.AddSprintToProjectInputDTO;
import com.elhadjium.PMBackend.dto.AddUserStoryDTO;
import com.elhadjium.PMBackend.dto.ErrorOutputDTO;
import com.elhadjium.PMBackend.dto.GetUserStoryOutputDTO;
import com.elhadjium.PMBackend.dto.InviteUsersToProjectInputDTO;
import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;
import com.elhadjium.PMBackend.dto.UpdateUsertStoryInputDTO;
import com.elhadjium.PMBackend.entity.Sprint;
import com.elhadjium.PMBackend.entity.UserStory;
import com.elhadjium.PMBackend.exception.PMRuntimeException;
import com.elhadjium.PMBackend.service.ProjectService;
import com.elhadjium.PMBackend.util.JavaUtil;

@RestController
@RequestMapping(PMConstants.PMBaseUri + "/projects")
public class ProjectController {
	@Autowired
	private ProjectService projectService;
	
	@PutMapping("{id}")
	public void updateProject(@RequestBody UpdateProjectInputDTO input, @PathVariable("id") String projectId) {
		projectService.updateProject(Long.valueOf(projectId), input);
	}
	
	@PostMapping("{id}/inviteUsers")
	public void inviteUsersToProject(@RequestBody InviteUsersToProjectInputDTO input, @PathVariable("id") String projectId) throws Exception {
		projectService.addInvitations(Long.valueOf(projectId), input);
	}
	
	@PostMapping("{project-id}/backlog/user-stories")
	public void createUserStoryInBacklog(@RequestBody AddUserStoryDTO input, @PathVariable("project-id") String projectId) throws Exception {
		projectService.addUserStrotyToBacklog(Long.parseLong(projectId), input);
	}
	
	@PostMapping("{project-id}/sprints/{sprint-id}/user-stories")
	public void createUserStoryInSprint(@RequestBody AddUserStoryDTO input, @PathVariable("sprint-id") String sprintId) throws Exception {
		projectService.addUserStoryToSprint(Long.parseLong(sprintId), input);
	}
	
	@DeleteMapping("{project-id}/user-stories/{user-story-id}")
	public void deleteUserStoryFromProject(@PathVariable("project-id") String projectId, @PathVariable("user-story-id") String userStoryId) throws Exception {
		projectService.deleteUserStoryFromProject(JavaUtil.parseId(projectId), JavaUtil.parseId(userStoryId));
	}
	
	@PutMapping("{project-id}/user-stories/{user-story-id}")
	public void updateUserStory(@PathVariable("project-id") String projectId,
								@PathVariable("user-story-id") String userStoryId,
								@RequestBody UpdateUsertStoryInputDTO inputDTO) {
		UserStory userStoryData = Mapping.mapTo(inputDTO, UserStory.class);
		
		projectService.updateUserStory(JavaUtil.parseId(projectId), JavaUtil.parseId(userStoryId), userStoryData);
	}
	
	@GetMapping("{project-id}/backlog/user-stories")
	public List<GetUserStoryOutputDTO> getBacklogUserStorires(@PathVariable("project-id") String projectId) {
		List<UserStory> userStoryDataList = projectService.getBacklogUserStories(JavaUtil.parseId(projectId));

		List<GetUserStoryOutputDTO> outputList = new ArrayList<GetUserStoryOutputDTO>();
		userStoryDataList.forEach(userStory -> {
			GetUserStoryOutputDTO output = Mapping.mapTo(userStory, GetUserStoryOutputDTO.class);
			outputList.add(output);
		});

		return outputList;
	}
	
	@GetMapping("{project-id}/sprints/{sprint-id}/user-stories")
	public List<GetUserStoryOutputDTO> getSprintUserStorires(@PathVariable("project-id") String projectId,
															@PathVariable("project-id") String sprintId) {
		List<UserStory> userStoryDataList = projectService.getSprintUserStories(JavaUtil.parseId(projectId),
																			    JavaUtil.parseId(sprintId));

		List<GetUserStoryOutputDTO> outputList = new ArrayList<GetUserStoryOutputDTO>();
		userStoryDataList.forEach(userStory -> {
			GetUserStoryOutputDTO output = Mapping.mapTo(userStory, GetUserStoryOutputDTO.class);
			outputList.add(output);
		});

		return outputList;
	}

	@PostMapping("{project-id}/sprints")
	public Long addSprintToProject(Long projectId, AddSprintToProjectInputDTO dto) {
		return projectService.addSprintToProject(projectId, Mapping.mapTo(dto, Sprint.class));
	}
	
	@PostMapping("{project-id}/user-stories/{user-story-id}/moveToBacklog")
	public void moveUserStoryFromSprintToBacklog(@PathVariable("project-id") String projectId,
												@PathVariable("user-story-id") String userStoryId) {
		projectService.moveUserStoryToBacklog(JavaUtil.parseId(projectId), JavaUtil.parseId(userStoryId));
	}

	@ExceptionHandler({PMRuntimeException.class})
	public ResponseEntity<?> handleException(PMRuntimeException ex) {
		ErrorOutputDTO errorOutputDTO = new ErrorOutputDTO();
		errorOutputDTO.setMessage(ex.getMessage());
		errorOutputDTO.setMessageDescription(ex.getMessage());

		return ResponseEntity.status(ex.getStatus()).body(errorOutputDTO);
	}
}
