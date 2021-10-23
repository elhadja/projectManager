package com.elhadjium.PMBackend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.UserProject;
import com.elhadjium.PMBackend.common.Mapping;
import com.elhadjium.PMBackend.dao.InvitationToProjectDAO;
import com.elhadjium.PMBackend.dao.ProjectDAO;
import com.elhadjium.PMBackend.dao.SprintDAO;
import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.dao.UserStoryDAO;
import com.elhadjium.PMBackend.dto.AddUserStoryDTO;
import com.elhadjium.PMBackend.dto.InviteUsersToProjectInputDTO;
import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;
import com.elhadjium.PMBackend.dto.UpdateUsertStoryInputDTO;
import com.elhadjium.PMBackend.entity.Backlog;
import com.elhadjium.PMBackend.entity.InvitationToProject;
import com.elhadjium.PMBackend.entity.User;
import com.elhadjium.PMBackend.entity.UserStory;
import com.elhadjium.PMBackend.exception.PMRuntimeException;

@Service
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	private ProjectDAO projectDao;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private SprintDAO sprintDAO;
	
	@Autowired
	private UserStoryDAO userStoryDAO;
	
	// TODO integration testing
	@Transactional
	public void updateProject(Long projectId, UpdateProjectInputDTO updateProjectInputDTO) {
		updateProjectInputDTO.validate();
		try {
			Project project = projectDao.findById(projectId).get();
			project.setName(updateProjectInputDTO.getProjectName());
			project.setDescription(updateProjectInputDTO.getProjectDescription());

			// get users to remove from project
			List<User> usersToRemoveFromProject = new ArrayList<User>();
			for (UserProject userProject: project.getUsers()) {
				if (!updateProjectInputDTO.getProjectUsersIds().contains(userProject.getUser().getId())) {
					usersToRemoveFromProject.add(userProject.getUser());
				}
			}
			// remove users
			for (User userToRemove: usersToRemoveFromProject) {
				project.removeUser(userToRemove);
			}

			// add users to project
			Map<Long, User> userCache = new HashMap<Long, User>();
			for (Long userId: updateProjectInputDTO.getProjectUsersIds()) {
				userCache.put(userId, userDAO.findById(userId).get());
				if (!project.getUsers().contains(new UserProject(userCache.get(userId), project))) {
					project.addUser(userCache.get(userId));
				} 
			}
			
			// actualize managers
			project.removeAllManagers();
			for (Long managerId: updateProjectInputDTO.getProjectManagersIds()) {
				User user = userCache.containsKey(managerId) ? userCache.get(managerId) : userDAO.findById(managerId).get();
				project.addManager(user);
			}
			
		} catch (NoSuchElementException e) {
			// TODO
		}
	}

	@Override
	@Transactional
	public void addInvitations(Long projectId, InviteUsersToProjectInputDTO input) {
		User author = userDAO.findById(input.getAuthorId()).get();
		boolean isManager = false;
		for (Project managedProject: author.getManagedProjects()) {
			if (managedProject.getId() == projectId) {
				isManager = true;
				break;
			}
		}
		if (!isManager) {
			throw new PMRuntimeException("You don't have access to this functionality", 400);
		}

		User guest = userDAO.findById(input.getGuestId()).get();
		guest.getProjects().forEach((project) -> {
			if (project.getProject().getId() == projectId) {
				throw new PMRuntimeException("User already on this project", 400);
			}
		});
		
		guest.getInvitationnToProject().forEach((invitation) -> {
			if (invitation.getProject().getId() == projectId) {
				throw new PMRuntimeException("An anvitation was already sent to this user", 400);
			}
		});
		
		InvitationToProject invitationToProject = new InvitationToProject();
		invitationToProject.setAuthor(author);
		projectDao.findById(projectId).get().addInvitation(invitationToProject);
		guest.addInvitationToProject(invitationToProject);
	}
	
	@Transactional
	@Override
	public long addUserStrotyToBacklog(Long projectId, AddUserStoryDTO userStoryDTO) {
		userStoryDTO.validate();
		UserStory us = Mapping.mapTo(userStoryDTO, UserStory.class);
		userStoryDAO.save(us);
		Backlog backlog = projectDao.findById(projectId).get().getBacklog();
		backlog.addUserStory(us);
		return us.getId();
	}
	
	//TODO integration testing
	@Override
	@Transactional
	public void addUserStoryToSprint(Long sprintId, AddUserStoryDTO userStoryDTO) {
		userStoryDTO.validate();
		UserStory us = Mapping.mapTo(userStoryDTO, UserStory.class);
		sprintDAO.findById(sprintId).get().addUserStory(us);
	}
	
	// FIXME US Could be in sprint and not in backlog
	@Transactional
	public void deleteUserStoryFromProject(long projectId, long userStoryId) {
		Project project = projectDao.findById(projectId).get();
		project.getBacklog().deleteUserStory(userStoryDAO.findById(userStoryId).get());
	}

	@Override
	public void updateUserStory(Long projectId, Long userStoryId, UserStory userStoryData) {
		UserStory userStoryToUpdate = userStoryDAO.findById(userStoryId).get();
		userStoryToUpdate.setSummary(userStoryData.getSummary());
		userStoryToUpdate.setDescription(userStoryData.getDescription());
		userStoryToUpdate.setStoryPoint(userStoryData.getStoryPoint());

		userStoryDAO.save(userStoryToUpdate);
	}
}