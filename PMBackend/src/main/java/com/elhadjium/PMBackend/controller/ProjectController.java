package com.elhadjium.PMBackend.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.elhadjium.PMBackend.dto.AddTaskInputDTO;
import com.elhadjium.PMBackend.dto.AddUserStoryDTO;
import com.elhadjium.PMBackend.dto.ErrorOutputDTO;
import com.elhadjium.PMBackend.dto.GetSprintOutputDTO;
import com.elhadjium.PMBackend.dto.GetTaskOutputDTO;
import com.elhadjium.PMBackend.dto.GetUserStoryOutputDTO;
import com.elhadjium.PMBackend.dto.GetUsersByCriteriaOutputDTO;
import com.elhadjium.PMBackend.dto.InviteUsersToProjectInputDTO;
import com.elhadjium.PMBackend.dto.StartSprintDTO;
import com.elhadjium.PMBackend.dto.TaskDepencieOutputDTO;
import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;
import com.elhadjium.PMBackend.dto.UpdateTaskDTO;
import com.elhadjium.PMBackend.dto.UpdateUsertStoryInputDTO;
import com.elhadjium.PMBackend.entity.Sprint;
import com.elhadjium.PMBackend.entity.Task;
import com.elhadjium.PMBackend.entity.TaskStatus;
import com.elhadjium.PMBackend.entity.UserStory;
import com.elhadjium.PMBackend.exception.PMRuntimeException;
import com.elhadjium.PMBackend.service.ProjectService;
import com.elhadjium.PMBackend.util.JavaUtil;

@RestController
@RequestMapping(PMConstants.PMBaseUri + "/projects")
public class ProjectController {
	@Autowired
	private ProjectService projectService;
	
	// TODO to test
	@PutMapping("{id}")
	public void updateProject(@RequestBody UpdateProjectInputDTO input, @PathVariable("id") String projectId) {
		projectService.updateProject(Long.valueOf(projectId), input);
	}
	
	// TODO to test
	@PostMapping("{id}/inviteUsers")
	public void inviteUsersToProject(@RequestBody InviteUsersToProjectInputDTO input,
									@PathVariable("id") String projectId) throws Exception {
		projectService.addInvitations(Long.valueOf(projectId), input);
	}
	
	@PostMapping("{project-id}/backlog/user-stories")
	public long createUserStoryInBacklog(@RequestBody AddUserStoryDTO input, @PathVariable("project-id") String projectId) throws Exception {
		return projectService.addUserStrotyToBacklog(Long.parseLong(projectId), input);
	}
	
	@PostMapping("{project-id}/sprints/{sprint-id}/user-stories")
	public long createUserStoryInSprint(@RequestBody AddUserStoryDTO input, @PathVariable("sprint-id") String sprintId) throws Exception {
		return projectService.addUserStoryToSprint(Long.parseLong(sprintId), input);
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

	// TODO to test
	@PostMapping("{project-id}/sprints")
	public Long addSprintToProject(@PathVariable("project-id") String projectId,@RequestBody AddSprintToProjectInputDTO dto) {
		dto.validate();
		return projectService.addSprintToProject(JavaUtil.parseId(projectId), Mapping.mapTo(dto, Sprint.class));
	}
	
	@PostMapping("{project-id}/backlog/user-stories/{user-story-id}")
	public void moveUserStoryFromSprintToBacklog(@PathVariable("project-id") String projectId,
												@PathVariable("user-story-id") String userStoryId) {
		projectService.moveUserStoryToBacklog(JavaUtil.parseId(projectId), JavaUtil.parseId(userStoryId));
	}
	
	@PostMapping("{project-id}/sprints/{sprint-id}/user-stories/{user-story-id}")
	public void moveUserStoryToSprint(@PathVariable("project-id") String projectId,
												@PathVariable("user-story-id") String userStoryId,
												@PathVariable("sprint-id") String sprintId) {
		projectService.moveUserStoryToSprint(JavaUtil.parseId(projectId), JavaUtil.parseId(sprintId), JavaUtil.parseId(userStoryId));
	}
	
	@PostMapping("{project-id}/tasks")
	public Long createTask(@PathVariable("project-id") String projectId,
							@RequestBody AddTaskInputDTO input) {
		input.validate();
		
		// FIXME mapping userId are setting all attribute with %id%
		Task taskData = Mapping.mapTo(input, Task.class);
		taskData.setId(null);
		// TODO should be refactored by defining an input type for the service
		if (input.getDependenciesIDs() != null) {
			for (Long dependencyId: input.getDependenciesIDs()) {
				Task dependency = new Task();
				dependency.setId(dependencyId);
				taskData.addDependency(dependency);
			}
		}
		
		// TODO should be refactored by defining an input type for the service
		if (input.getUserStoriesIDs() != null) {
			taskData.getTaskUserStories().clear();
			for (Long usId: input.getUserStoriesIDs()) {
				UserStory us = new UserStory();
				us.setId(usId);
				taskData.addUserStory(us);
			}
		}

		return projectService.createTask(taskData);
	}
	
	@Deprecated
	@DeleteMapping("{project-id}/user-stories/{user-story-id}/tasks/{tasks-ids}")
	public void removeUserStoryTasks(@PathVariable("project-id") String projectId,
							@PathVariable("user-story-id") String userStoryId,
							@PathVariable("tasks-ids") String[] taskIDs) {
		for (String taskId: taskIDs) {
			projectService.removeTask(JavaUtil.parseId(userStoryId), JavaUtil.parseId(taskId));
		}
	}
	
	@DeleteMapping("{project-id}/tasks/{tasks-ids}")
	public void removeTasks(@PathVariable("project-id") String projectId, @PathVariable("tasks-ids") String[] taskIDs) {
		projectService.removeTask(Arrays.asList(taskIDs).stream().map(taskId -> JavaUtil.parseId(taskId)).collect(Collectors.toSet()));
	}
	
	@GetMapping("{project-id}/sprints/{sprint-id}/tasks")
	public Set<GetTaskOutputDTO> getSprintTasks(@PathVariable("sprint-id") String sprintId) {
		Set<Task> tasks = projectService.getSprintTasks(JavaUtil.parseId(sprintId));
		Set<GetTaskOutputDTO> outputList = new HashSet<GetTaskOutputDTO>();
		tasks.forEach(task -> {
			GetTaskOutputDTO output = Mapping.mapTo(task, GetTaskOutputDTO.class);
			// TODO to refactor
			Set<TaskDepencieOutputDTO> dependenciesOutputDTO = Arrays.asList(Mapping.mapTo(task.getDependencies(), TaskDepencieOutputDTO[].class))
										.stream()
										.map(taskDependendyOutptuDTO -> (TaskDepencieOutputDTO)taskDependendyOutptuDTO)
										.collect(Collectors.toSet());
			output.setDependencies(dependenciesOutputDTO);
			outputList.add(output);
		});
		
		return outputList;
	}
	
	@GetMapping("{project-id}/sprints")
	public List<GetSprintOutputDTO> getProjectSprints(@PathVariable("project-id") String projectId) {
		List<GetSprintOutputDTO> outputList = new ArrayList<GetSprintOutputDTO>();
		List<Sprint> sprints = projectService.getProjectSprints(JavaUtil.parseId(projectId));
		sprints.forEach(sprint -> {
			outputList.add(Mapping.mapTo(sprint, GetSprintOutputDTO.class));
		});

		return outputList;
	}
	
	@DeleteMapping("{project-id}/sprints/{sprint-id}")
	public void deleteSprint(@PathVariable("") String projectId, @PathVariable("") String sprintId) {
		projectService.deleteSprint(JavaUtil.parseId(projectId), JavaUtil.parseId(sprintId));
	}
	
	@PutMapping("{project-id}/sprints/{sprint-id}/start")
	public void startSprint(@PathVariable("project-id") String projectId,
							@PathVariable("sprint-id") String sprintId,
							@RequestBody StartSprintDTO input) {
		projectService.startSprint(JavaUtil.parseId(projectId), JavaUtil.parseId(sprintId), input);
	}
	
	@PutMapping("{project-id}/sprints/{sprint-id}/terminate")
	public void terminateSprint(@PathVariable("project-id") String projectId, @PathVariable("sprint-id") String sprintId) {
		projectService.terminateSprint(JavaUtil.parseId(projectId), JavaUtil.parseId(sprintId));
	}
	
	@PutMapping("{project-id}/user-stories/{user-story-id}/close")
	public void closeUserStory(@PathVariable("project-id") String projectId, @PathVariable("user-story-id") String userStoryId) {
		projectService.closeUserStory(JavaUtil.parseId(projectId), JavaUtil.parseId(userStoryId));
	}

	@PutMapping("{project-id}/user-stories/{user-story-id}/open")
	public void openUserStory(@PathVariable("project-id") String projectId, @PathVariable("user-story-id") String userStoryId) {
		projectService.openUserStory(JavaUtil.parseId(projectId), JavaUtil.parseId(userStoryId));
	}
	
	@PutMapping("{project-id}/tasks/{task-id}")
	public GetTaskOutputDTO updateTask(@PathVariable("project-id") String projectId, 
						   @PathVariable("task-id") String taskId,
						   @RequestBody UpdateTaskDTO input) throws Exception {
		// FIXME mapping userId are setting all attribute with %id%
		Task taskData = Mapping.mapTo(input, Task.class);
		taskData.setId(null);
		// TODO should be refactored by defining an input type for the service
		if (input.getDependenciesIDs() != null) {
			for (Long dependencyId: input.getDependenciesIDs()) {
				Task dependency = new Task();
				dependency.setId(dependencyId);
				taskData.addDependency(dependency);
			}
		}
		
		if (input.getUserStoriesIDs() != null) {
			taskData.getTaskUserStories().clear();
			for (Long userStoryId: input.getUserStoriesIDs()) {
				UserStory us = new UserStory();
				us.setId(userStoryId);
				taskData.addUserStory(us);
			}
		}

		Task updatedTask = projectService.updateTask(JavaUtil.parseId(taskId), taskData);

		// TODO respect the contract
		return null;
	}
	
	@PutMapping("{project-id}/tasks/{task-id}/")
	public void setTaskStatus(@PathVariable("task-id") String taskId, @RequestBody TaskStatus status) throws Exception {
		projectService.setTaskStatus(JavaUtil.parseId(taskId),status);
	}
	
	// TODO to be tested
	@GetMapping("{project-id}/users")
	public List<GetUsersByCriteriaOutputDTO> getProjectUsers(@PathVariable("project-id")String projectId) {
		return Arrays.asList(Mapping.mapTo(projectService.getProjectUsers(JavaUtil.parseId(projectId)), GetUsersByCriteriaOutputDTO[].class));
	}

	@ExceptionHandler({PMRuntimeException.class})
	public ResponseEntity<?> handleException(PMRuntimeException ex) {
		ErrorOutputDTO errorOutputDTO = new ErrorOutputDTO();
		errorOutputDTO.setMessage(ex.getMessage());
		errorOutputDTO.setMessageDescription(ex.getMessage());

		return ResponseEntity.status(ex.getStatus()).body(errorOutputDTO);
	}
}
