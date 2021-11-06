package com.elhadjium.PMBackend.unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.elhadjium.PMBackend.controller.ProjectController;
import com.elhadjium.PMBackend.dto.AddSprintToProjectInputDTO;
import com.elhadjium.PMBackend.dto.AddTaskInputDTO;
import com.elhadjium.PMBackend.dto.AddUserStoryDTO;
import com.elhadjium.PMBackend.dto.ErrorOutputDTO;
import com.elhadjium.PMBackend.dto.GetSprintOutputDTO;
import com.elhadjium.PMBackend.dto.GetTaskOutputDTO;
import com.elhadjium.PMBackend.dto.GetUserStoryOutputDTO;
import com.elhadjium.PMBackend.dto.InviteUsersToProjectInputDTO;
import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;
import com.elhadjium.PMBackend.dto.UpdateUsertStoryInputDTO;
import com.elhadjium.PMBackend.entity.Sprint;
import com.elhadjium.PMBackend.entity.Task;
import com.elhadjium.PMBackend.entity.User;
import com.elhadjium.PMBackend.entity.UserStory;
import com.elhadjium.PMBackend.entity.UserStoryImportance;
import com.elhadjium.PMBackend.entity.UserStoryStatus;
import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.exception.PMRuntimeException;
import com.elhadjium.PMBackend.service.ProjectService;
import com.elhadjium.PMBackend.service.UserService;
import com.elhadjium.PMBackend.util.JwtToken;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers =  ProjectController.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProjectControllerTest {
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	JwtToken jwtToken;
	
	@MockBean
	private ProjectService projectService;
	
	@MockBean
	private UserService userService;
	
	private ProjectController projectController = new ProjectController();
	
	@Test
	public void getSprintTasks_shouldBeOk() throws Exception {
		Task task = new Task();
		task.setId(1L);
		task.setDescription("mqlksdjfqmlkdf");
		task.setDuration(1.5f);
		User user = new User();
		user.setId(1L);
		user.setPseudo("pseudo");
		task.setUser(user);

		when(projectService.getSprintTasks(Mockito.anyLong())).thenReturn(Set.of(task));

		// when
		MvcResult res = this.mockMvc.perform(get("/pm-api/projects/1/sprints/1/tasks").contentType(MediaType.APPLICATION_JSON))
					.andDo(print())
					.andExpect(status().isOk())
					.andReturn();

		List<GetTaskOutputDTO> tasks = Arrays.asList(getObject(res, GetTaskOutputDTO[].class));
		
		//then
		assertNotNull(tasks);
		assertEquals(1, tasks.size());
		GetTaskOutputDTO output = tasks.get(0);

		assertEquals(task.getId(), output.getId());
		assertEquals(task.getDescription(), output.getDescription());
		assertEquals(task.getDuration(), output.getDuration());
		assertEquals(task.getUser().getPseudo(), output.getUserPseudo());
	}
	
	@Test
	public void getBacklogUserStories_shouldBeOk() throws Exception {
		// prepare
		final Long projectId = 1L;

		UserStory us1 = new UserStory();
		us1.setId(1L);
		us1.setSummary("summary 1");
		us1.setDescription("desc");

		UserStory us2 = new UserStory();
		us1.setId(2L);
		us1.setSummary("summary 2");
		us1.setDescription("desc 2");
		us1.setStatus(UserStoryStatus.CLOSE);
		us1.setImportance(UserStoryImportance.HIGHT);
	
		when(projectService.getBacklogUserStories(projectId)).thenReturn(List.of(us1, us2));
		
		// when
		MvcResult res = this.mockMvc.perform(get("/pm-api/projects/" + projectId + "/backlog/user-stories").contentType(MediaType.APPLICATION_JSON))
					.andDo(print())
					.andExpect(status().isOk())
					.andReturn();
		List<GetUserStoryOutputDTO> userStories = Arrays.asList(getObject(res, GetUserStoryOutputDTO[].class));
		
		// then
		assertNotNull(userStories);
		assertEquals(2, userStories.size());
		GetUserStoryOutputDTO usToCheck = userStories.get(0);
		assertEquals(us1.getId(), usToCheck.getId());
		assertEquals(us1.getSummary(), usToCheck.getSummary());
		assertEquals(us1.getDescription(), usToCheck.getDescription());
		assertEquals(us1.getStatus().CLOSE, usToCheck.getStatus());
		assertEquals(us1.getImportance(), usToCheck.getImportance());
	}
	
	@Test
	public void updateUserStory_shouldBeOk() throws Exception {
		// prepare
		final long projectId = 1L;
		final long userStoryId = 2L;
		
		UpdateUsertStoryInputDTO input = new UpdateUsertStoryInputDTO();
		input.setSummary("summary");
		input.setStatus(UserStoryStatus.CLOSE);

		// when
		this.mockMvc.perform(put("/pm-api/projects/" + projectId + "/user-stories/" + userStoryId).contentType(MediaType.APPLICATION_JSON).content(stringify(input)))
					.andDo(print())
					.andExpect(status().isOk())
					.andReturn();
		
		// then
		verify(projectService).updateUserStory(Mockito.eq(projectId), Mockito.eq(userStoryId), Mockito.any(UserStory.class));
	}
	
	@Test
	public void deleteUserStoryFromProjectShouldBeOk() throws Exception {
		// prepare
		final long projectId = 1L;
		final long userStoryId = 2L;
	
		// when
		this.mockMvc.perform(delete("/pm-api/projects/" + projectId + "/user-stories/" + userStoryId).contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
	
		// then
		verify(projectService).deleteUserStoryFromProject(Mockito.eq(projectId), Mockito.eq(userStoryId));
	}
	
	@Test
	public void createUserStoryInSprint_shouldBeOk() throws Exception {
		// prepare
		final long projectId = 1L;
		final long sprintId = 2L;
		
		AddUserStoryDTO input = new AddUserStoryDTO();

		// when
		this.mockMvc.perform(post("/pm-api/projects/" + projectId + "/sprints/" + sprintId + "/user-stories")
							.contentType(MediaType.APPLICATION_JSON)
							.content(stringify(input)))
					.andDo(print())
					.andExpect(status().isOk())
					.andReturn();
	
		// then
		verify(projectService).addUserStoryToSprint(Mockito.eq(sprintId), Mockito.any(AddUserStoryDTO.class));
	}
	
	@Test
	public void createUserStoryInBacklog_shouldBeOk() throws Exception {
		// prepare
		final long projectId = 1L;
		
		AddUserStoryDTO input = new AddUserStoryDTO();

		// when
		this.mockMvc.perform(post("/pm-api/projects/" + projectId + "/backlog/user-stories")
							.contentType(MediaType.APPLICATION_JSON)
							.content(stringify(input)))
					.andDo(print())
					.andExpect(status().isOk())
					.andReturn();
	
		// then
		verify(projectService).addUserStrotyToBacklog(Mockito.eq(projectId), Mockito.any(AddUserStoryDTO.class));
	}
	
	@Test
	public void addSprintToProject_shoulbeOk() throws Exception {
		// prepare
		final long projectId = 1L;
		
		AddSprintToProjectInputDTO input = new AddSprintToProjectInputDTO();
		input.setName("sprint name");
		input.setStartDate("2021-10-30 16:27");
		input.setEndDate("2021-11-30 16:27");
		
		// when
		this.mockMvc.perform(post("/pm-api/projects/" + projectId + "/sprints")
							.contentType(MediaType.APPLICATION_JSON)
							.content(stringify(input)))
					.andDo(print())
					.andExpect(status().isOk())
					.andReturn();
	
		// then
		verify(projectService).addSprintToProject(Mockito.eq(projectId), Mockito.any(Sprint.class));
	}
	
	@Test
	public void getProjectSprint_shouldBeOk() throws Exception {
		// prepare
		final long projectId = 1L;
		
		Sprint sprint = new Sprint();
		sprint.setName("sprint name");
		sprint.setStartDate("2021-10-30 16:27");
		sprint.setEndDate("2021-11-30 16:27");

		when(projectService.getProjectSprints(projectId)).thenReturn(List.of(sprint));

		// when
		MvcResult res = this.mockMvc.perform(get("/pm-api/projects/" + projectId + "/sprints")
							.contentType(MediaType.APPLICATION_JSON))
					.andDo(print())
					.andExpect(status().isOk())
					.andReturn();
		
		// then
		List<GetSprintOutputDTO> output = Arrays.asList(getObject(res, GetSprintOutputDTO[].class));
		assertNotNull(output);
		assertEquals(1, output.size());
		assertEquals(sprint.getName(), output.get(0).getName());
		assertEquals(sprint.getStartDate(), output.get(0).getStartDate());
	}
	
	@Test
	public void updateProject_shouldBeOk() throws Exception {
		// prepare
		final long projectId = 1L;
		
		UpdateProjectInputDTO input = new UpdateProjectInputDTO();
		input.setProjectName("project name");
		input.setProjectManagersIds(List.of(1L));
		
		// when
		this.mockMvc.perform(put("/pm-api/projects/" + projectId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(stringify(input)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		// then
		verify(projectService).updateProject(Mockito.eq(projectId), Mockito.any(UpdateProjectInputDTO.class));
	}
	
	@Test
	public void inviteUserToProject_shouldBeOk() throws Exception {
		// prepare
		final long projectId = 1L;
		
		InviteUsersToProjectInputDTO input = new InviteUsersToProjectInputDTO();
		
		// when
		this.mockMvc.perform(post("/pm-api/projects/" + projectId + "/inviteUsers")
						.contentType(MediaType.APPLICATION_JSON)
						.content(stringify(input)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		// then
		verify(projectService).addInvitations(Mockito.eq(projectId), Mockito.any(InviteUsersToProjectInputDTO.class));
	}
	
	@Test
	public void moveUserStoryToSprint_shouldBeOk() throws Exception {
		// prepare
		final long projectId = 1;
		final long sprintId = 2;
		final long userStoryId = 3;

		// when
		this.mockMvc.perform(post("/pm-api/projects/" + projectId + "/sprints/" + sprintId + "/user-stories/" + userStoryId)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		
		// then 
		verify(projectService).moveUserStoryToSprint(Mockito.eq(projectId), Mockito.eq(sprintId), Mockito.eq(userStoryId));
	}
	
	@Test
	public void createTask_shouldBeOk() throws Exception {
		// prepare
		final long projectId = 1;
		final long userStoryId = 2;
		
		AddTaskInputDTO input = new AddTaskInputDTO();
		input.setDescription("task description");
		input.setUserId(1);
		input.setUserStoryId(2);

		// when
		this.mockMvc.perform(post("/pm-api/projects/" + projectId + "/user-stories/" + userStoryId + "/tasks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(stringify(input)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		
		// then 
		verify(projectService).createTask(Mockito.eq(userStoryId), Mockito.any(Task.class));
	}
	
	@Test
	public void deleteTask_shouldBeOk() throws Exception {
		// prepare
		final long projectId = 1;
		final long userStoryId = 2;
		final long taskId = 3;
		
		AddTaskInputDTO input = new AddTaskInputDTO();
		input.setDescription("task description");
		input.setUserId(1);
		input.setUserStoryId(2);

		// when
		this.mockMvc.perform(delete("/pm-api/projects/" + projectId + "/user-stories/" + userStoryId + "/tasks/" + taskId)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		
		// then 
		verify(projectService).removeTask(Mockito.eq(userStoryId), Mockito.eq(taskId));
	}
	
	@Test
	public void handleException_shouldBeOk() throws Exception {
		// when
		ResponseEntity<?> response = projectController.handleException(new PMInvalidInputDTO(""));
		
		// then
		assertEquals(400, response.getStatusCodeValue());
	}
	
	@Test
	public void startSprint_shouldBeOK() throws Exception {
		// prepare
		final long projectId = 1;
		final long sprintId = 2;

		// when
		putRequest("/pm-api/projects/" + projectId + "/sprints/" + sprintId + "/start", null);
		
		// then
		verify(projectService).startSprint(Mockito.eq(projectId), Mockito.eq(sprintId));
	}
	
	@Test
	public void terminateSprint_shouldBeOK() throws Exception {
		// prepare
		final long projectId = 1;
		final long sprintId = 2;

		// when
		putRequest("/pm-api/projects/" + projectId + "/sprints/" + sprintId + "/terminate", null);
		
		// then
		verify(projectService).terminateSprint(Mockito.eq(projectId), Mockito.eq(sprintId));
	}
	
	private <T> T getObject(MvcResult mvcResult, Class<T> targetClass) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(mvcResult.getResponse().getContentAsString(), targetClass);
	}
	
	private String stringify(Object object) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}
	
	private <T> MvcResult putRequest (String uri, T input) throws Exception {
		return this.mockMvc.perform(put(uri)
				.contentType(MediaType.APPLICATION_JSON)
				.content(stringify(input)))
		.andDo(print())
		.andExpect(status().isOk())
		.andReturn();
	}
}
