package com.elhadjium.PMBackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elhadjium.PMBackend.common.PMConstants;
import com.elhadjium.PMBackend.dto.AddUserStoryDTO;
import com.elhadjium.PMBackend.dto.ErrorOutputDTO;
import com.elhadjium.PMBackend.dto.InviteUsersToProjectInputDTO;
import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;
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
	
	@PostMapping("{project-id}/backlog/user-story")
	public void createUserStoryInBacklog(@RequestBody AddUserStoryDTO input, @PathVariable("project-id") String projectId) throws Exception {
		projectService.addUserStrotyToBacklog(Long.parseLong(projectId), input);
	}
	
	@PostMapping("{project-id}/sprint/{sprint-id}/user-story")
	public void createUserStoryInSprint(@RequestBody AddUserStoryDTO input, @PathVariable("sprint-id") String sprintId) throws Exception {
		projectService.addUserStoryToSprint(Long.parseLong(sprintId), input);
	}
	
	@DeleteMapping("{project-id}/user-story/{user-story-id}")
	public void deleteUserStoryFromProject(@PathVariable("project-id") String projectId, @PathVariable("user-story-id") String userStoryId) throws Exception {
		projectService.deleteUserStoryFromProject(JavaUtil.parseId(projectId), JavaUtil.parseId(userStoryId));
	}

	@ExceptionHandler({PMRuntimeException.class})
	public ResponseEntity<?> handleException(PMRuntimeException ex) {
		ErrorOutputDTO errorOutputDTO = new ErrorOutputDTO();
		errorOutputDTO.setMessage(ex.getMessage());
		errorOutputDTO.setMessageDescription(ex.getMessage());

		return ResponseEntity.status(ex.getStatus()).body(errorOutputDTO);
	}
}
