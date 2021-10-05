package com.elhadjium.PMBackend.unitTests.services;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.elhadjium.PMBackend.dao.ProjectDAO;
import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;
import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.service.ProjectService;
import com.elhadjium.PMBackend.service.ProjectServiceImpl;

@ExtendWith(SpringExtension.class)
public class ProjectServiceTest {
	@Mock
	private ProjectDAO projectDAO;
	
	@Mock
	private UserDAO userDAO;
	
	@InjectMocks
	private ProjectService projectService = new ProjectServiceImpl();
	
	@Test
	public void updateProject_shouldFailWhenTringToRemoveAllUsers() throws Exception {
		// prepare
		UpdateProjectInputDTO input = new UpdateProjectInputDTO();
		input.setProjectManagersIds(Arrays.asList(1L));
		input.setProjectUsersIds(new ArrayList<Long>());
		try {
			// when
			projectService.updateProject(1L, input);
			fail();
		} catch (PMInvalidInputDTO e) {
		}
	}
	
	@Test
	public void updateProject_shouldFailWhenTringToRemoveAllManagers() throws Exception {
		// prepare
		UpdateProjectInputDTO input = new UpdateProjectInputDTO();
		input.setProjectUsersIds(Arrays.asList(1L));
		input.setProjectManagersIds(new ArrayList<Long>());
		try {
			// when
			projectService.updateProject(1L, input);
			fail();
		} catch (PMInvalidInputDTO e) {
		}
	}
}
