package com.elhadjium.PMBackend.integrationTests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.dao.ProjectDAO;
import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;
import com.elhadjium.PMBackend.service.ProjectService;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProjectServiceTest {
	@Autowired
	ProjectService projectService;
	
	@Autowired
	UserDAO testDAO;
	
	@Autowired
	ProjectDAO projectDAO;
	
	@Autowired
	UserDAO userDAO;
	
	@Test
	@Sql("/data.sql")
	public void test() throws Exception {
	}
	
	@Test
	@Sql("/data.sql")
	@Transactional
	public void updateProject_shouldRemoveSomeUsersAndManagers() throws Exception {
		// prepare
		UpdateProjectInputDTO input = new UpdateProjectInputDTO();
		input.setProjectName("new name");
		input.setProjectManagersIds(List.of(1L));
		input.setProjectUsersIds(List.of(1L, 2L));
		// when
		projectService.updateProject(1L, input);
		
		// then
		Project project = projectDAO.findById(1L).get();
		assertEquals(2, project.getUsers().size());
		assertEquals(1, project.getManagers().size());
	}
}
