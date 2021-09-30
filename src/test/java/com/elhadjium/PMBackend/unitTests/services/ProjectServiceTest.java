package com.elhadjium.PMBackend.unitTests.services;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.UserProject;
import com.elhadjium.PMBackend.dao.ProjectDAO;
import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;
import com.elhadjium.PMBackend.entity.User;

@ExtendWith(SpringExtension.class)
public class ProjectServiceTest {
	@Mock
	private ProjectDAO projectDAO;
	
	@Mock
	private UserDAO userDAO;
	
	/*
	public void updateProject_shouldRemoveSomeUsersToTheProject() throws Exception {
		// prepare
		long projectId = 1;

		User user1 = new User(1L, null, null, null, null, null);
		User user2 = new User(2L, null, null, null, null, null);
		User user3 = new User(3L, null, null, null, null, null); // to remove
		User user4 = new User(4L, null, null, null, null, null); // to remove

		Project project = new Project();
		project.setId(projectId);
		
		UserProject userProject1 = new UserProject(user1, project);
		UserProject userProject2 = new UserProject(user2, project);
		UserProject userProject3 = new UserProject(user3, project);
		UserProject userProject4 = new UserProject(user4, project);

		UpdateProjectInputDTO input = new UpdateProjectInputDTO();
		input.setProjectName("project luna");
		input.setProjectManagersIds(List.of(1L));
		input.setProjectUsersIds(List.of(1L, 2L));
		
		// mock
		when(projectDAO.findById(projectId)).thenReturn(Optional.of(new Project()));

		
		
	}
	*/

}
