package com.elhadjium.PMBackend.unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.elhadjium.PMBackend.controller.ProjectController;
import com.elhadjium.PMBackend.dto.GetTaskOutputDTO;
import com.elhadjium.PMBackend.entity.Task;
import com.elhadjium.PMBackend.entity.User;
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

		ObjectMapper mapper = new ObjectMapper();
		List<GetTaskOutputDTO> tasks = Arrays.asList(mapper.readValue(res.getResponse().getContentAsString(), GetTaskOutputDTO[].class));
		
		//then
		assertNotNull(tasks);
		assertEquals(1, tasks.size());
		GetTaskOutputDTO output = tasks.get(0);

		assertEquals(task.getId(), output.getId());
		assertEquals(task.getDescription(), output.getDescription());
		assertEquals(task.getDuration(), output.getDuration());
		assertEquals(task.getUser().getPseudo(), output.getUserPseudo());
	}
}
