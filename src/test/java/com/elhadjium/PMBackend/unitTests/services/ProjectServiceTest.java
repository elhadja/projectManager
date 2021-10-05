package com.elhadjium.PMBackend.unitTests.services;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.UserProject;
import com.elhadjium.PMBackend.dao.ProjectDAO;
import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.dto.InviteUsersToProjectInputDTO;
import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;
import com.elhadjium.PMBackend.entity.InvitationToProject;
import com.elhadjium.PMBackend.entity.User;
import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.exception.PMRuntimeException;
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
	
	@Test
	public void addInvitation_shouldFailWhenUserHasNoRight() throws Exception {
		//prepare
		final long projectId = 1;
		User author = new User();
		author.setId(1L);
				
		InviteUsersToProjectInputDTO input = new InviteUsersToProjectInputDTO();
		input.setAuthorId(author.getId());
		
		//mock
		when(userDAO.findById(author.getId())).thenReturn(Optional.of(author));
		
		// when
		try {
			projectService.addInvitations(1L, input);
			fail();
		} catch (PMRuntimeException e) {
			// TODO: handle exception
		}
	}
	
	@Test
	public void addInvitation_shouldFailWhenUserAreAlreadyAUserOfTheProject() throws Exception {
		//prepare
		Project project = new Project();
		project.setId(1L);

		User author = new User();
		author.setId(1L);
		author.setManagedProjects(Arrays.asList(project));

		User guest = new User();
		guest.setId(2L);
		guest.setProjects(Arrays.asList(new UserProject(guest, project)));

		
		InviteUsersToProjectInputDTO input = new InviteUsersToProjectInputDTO();
		input.setAuthorId(1L);
		input.setGuestId(2L);
		
		//mock
		when(userDAO.findById(author.getId())).thenReturn(Optional.of(author));
		when(userDAO.findById(guest.getId())).thenReturn(Optional.of(guest));
		
		// when
		try {
			projectService.addInvitations(project.getId(), input);
			fail();
		} catch (PMRuntimeException e) {
			verify(userDAO).findById(guest.getId());
		}
	}
	
	@Test
	public void addInvitation_shouldFailWhenUserHasAnInvitationOnThisProject() throws Exception {
		//prepare
		Project project = new Project();
		project.setId(1L);

		User author = new User();
		author.setId(1L);
		author.setManagedProjects(Arrays.asList(project));

		User guest = new User();
		guest.setId(2L);
		guest.setProjects(new ArrayList<UserProject>());

		InvitationToProject invitationDB = new InvitationToProject();
		invitationDB.setAuthor(author);
		invitationDB.setGuest(guest);
		invitationDB.setProject(project);

		guest.setInvitationnToProject(Arrays.asList(invitationDB));

		
		InviteUsersToProjectInputDTO input = new InviteUsersToProjectInputDTO();
		input.setAuthorId(1L);
		input.setGuestId(2L);
		
		//mock
		when(userDAO.findById(author.getId())).thenReturn(Optional.of(author));
		when(userDAO.findById(guest.getId())).thenReturn(Optional.of(guest));
		
		// when
		try {
			projectService.addInvitations(project.getId(), input);
			fail();
		} catch (PMRuntimeException e) {
			verify(userDAO).findById(guest.getId());
			verify(userDAO).findById(author.getId());
		}
	}
}
