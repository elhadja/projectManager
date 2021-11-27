package com.elhadjium.PMBackend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.UserProject;
import com.elhadjium.PMBackend.common.Mapping;
import com.elhadjium.PMBackend.dao.ProjectDAO;
import com.elhadjium.PMBackend.dao.SprintDAO;
import com.elhadjium.PMBackend.dao.TaskDAO;
import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.dao.UserStoryDAO;
import com.elhadjium.PMBackend.dto.AddUserStoryDTO;
import com.elhadjium.PMBackend.dto.InviteUsersToProjectInputDTO;
import com.elhadjium.PMBackend.dto.StartSprintDTO;
import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;
import com.elhadjium.PMBackend.entity.Backlog;
import com.elhadjium.PMBackend.entity.InvitationToProject;
import com.elhadjium.PMBackend.entity.Sprint;
import com.elhadjium.PMBackend.entity.SprintStatus;
import com.elhadjium.PMBackend.entity.Task;
import com.elhadjium.PMBackend.entity.TaskStatus;
import com.elhadjium.PMBackend.entity.User;
import com.elhadjium.PMBackend.entity.UserStory;
import com.elhadjium.PMBackend.entity.UserStoryStatus;
import com.elhadjium.PMBackend.entity.UserStoryTasK;
import com.elhadjium.PMBackend.exception.PMRuntimeException;

@Service
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	private UserStoryService userStoryService;
	
	@Autowired TaskService taskService;
	
	@Autowired
	private ProjectDAO projectDao;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private SprintDAO sprintDAO;
	
	@Autowired
	private UserStoryDAO userStoryDAO;
	
	@Autowired
	private TaskDAO taskDAO;
	
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
		us.setStatus(UserStoryStatus.OPEN);
		userStoryDAO.save(us);
		Backlog backlog = projectDao.findById(projectId).get().getBacklog();
		backlog.addUserStory(us);
		return us.getId();
	}
	
	//TODO integration testing
	@Override
	@Transactional
	public long addUserStoryToSprint(Long sprintId, AddUserStoryDTO userStoryDTO) {
		userStoryDTO.validate();
		UserStory us = Mapping.mapTo(userStoryDTO, UserStory.class);
		us.setStatus(UserStoryStatus.OPEN);
		sprintDAO.findById(sprintId).get().addUserStory(us);
		userStoryDAO.save(us);
		return us.getId();
	}
	
	// FIXME US Could be in sprint and not in backlog
	@Transactional
	public void deleteUserStoryFromProject(long projectId, long userStoryId) {
		UserStory userStoryToDelete = userStoryDAO.findById(userStoryId).get();
		if (userStoryToDelete.getBacklog() != null) {
			userStoryToDelete.getBacklog().deleteUserStory(userStoryToDelete);
		} else {
			userStoryToDelete.getSprint().removeUserStory(userStoryToDelete);
		}
		
		userStoryDAO.delete(userStoryToDelete);
	}

	@Override
	public void updateUserStory(Long projectId, Long userStoryId, UserStory userStoryData) {
		UserStory userStoryToUpdate = userStoryDAO.findById(userStoryId).get();
		userStoryToUpdate.setSummary(userStoryData.getSummary());
		userStoryToUpdate.setDescription(userStoryData.getDescription());
		userStoryToUpdate.setStoryPoint(userStoryData.getStoryPoint());
		userStoryToUpdate.setStatus(userStoryData.getStatus());
		userStoryToUpdate.setImportance(userStoryData.getImportance());

		userStoryDAO.save(userStoryToUpdate);
	}

	@Override
	public List<UserStory> getBacklogUserStories(Long projectId) {
		return projectDao.findById(projectId).get().getBacklog().getUserStories();
	}

	@Override
	public List<UserStory> getSprintUserStories(Long parseId, Long sprintId) {
		return userStoryDAO.findBySprintId(sprintId);
	}

	@Override
	@Transactional
	public Long addSprintToProject(Long projectId, Sprint sprintData) {
		Sprint sprint = Mapping.mapTo(sprintData, Sprint.class);
		sprint.setStatus(SprintStatus.CREATED);
		Project project = projectDao.findById(projectId).get();
		project.addSprint(sprint);
		sprintDAO.save(sprint);

		return sprint.getId();
	}

	@Override
	@Transactional
	public void moveUserStoryToBacklog(Long projectId, Long userStoryId) {
		Project project = projectDao.findById(projectId).get();
		UserStory us = userStoryDAO.findById(userStoryId).get();

		if (us.getBacklog() == null && us.getSprint() != null) {
			us.getSprint().removeUserStory(us);
			project.getBacklog().addUserStory(us);
		}
	}

	@Override
	@Transactional
	public void moveUserStoryToSprint(Long projectId, Long sprintId, Long userStoryId) {
		Project project = projectDao.findById(projectId).get();
		UserStory us = userStoryDAO.findById(userStoryId).get();
		
		if (us.getSprint() != null) {
			us.getSprint().removeUserStory(us);
		} else if (us.getBacklog() != null) {
			us.getBacklog().deleteUserStory(us);
		}
		
		Sprint sprint = sprintDAO.findById(sprintId).get();
		sprint.addUserStory(us);
	}

	@Override
	@Transactional
	public Long createTask(Long userStoryId, Task taskData) {
		Task task = Mapping.mapTo(taskData, Task.class);
		UserStory us = userStoryDAO.findById(userStoryId).get();
		task.addUserStory(us);
		taskDAO.save(task);
		
		return task.getId();
	}

	@Override
	@Transactional
	public void removeTask(Long userStoryId, Long taskId) {
		Task task = taskDAO.findById(taskId).get();
		task.removeUserStory(userStoryDAO.findById(userStoryId).get());
		taskDAO.delete(task);
	}

	@Override
	@Transactional
	public Set<Task> getSprintTasks(Long sprintId) {
		Sprint sprint = sprintDAO.findById(sprintId).get();
		Set<Task> tasks = new HashSet<Task>();
		Iterator<UserStory> it = sprint.getUserStories().iterator();
		while(it.hasNext()) {
			tasks.addAll(it.next().getUserStoryTasks().stream()
														.map(userStoryMap -> userStoryMap.getTask())
														.collect(Collectors.toSet()));
		}

		return tasks;
	}

	@Override
	@Transactional
	public List<Sprint> getProjectSprints(Long projectId) {
		return projectDao.findById(projectId).get().getSprints();
	}

	@Override
	@Transactional
	public void deleteSprint(long projectId, long sprintId) {
		Project project = projectDao.findById(projectId).get();
		Sprint sprintToDelete = sprintDAO.findById(sprintId).get();
		Iterator<UserStory> it = sprintToDelete.getUserStories().iterator();
		while (it.hasNext()) {
			UserStory us = it.next();
			project.getBacklog().addUserStory(us);
			us.setSprint(null);
		}
		sprintToDelete.getUserStories().clear();
		
		project.removeSprint(sprintToDelete);
	}
	
	@Override
	@Transactional
	public void startSprint(Long projectId, Long sprintId, StartSprintDTO input) {
		Sprint sprint = sprintDAO.findById(sprintId).get();
		sprint.setStatus(SprintStatus.STARTED);
		sprint.setStartDate(input.getStartDate());
		sprint.setEndDate(input.getEndDate());
	}

	@Override
	@Transactional
	public void terminateSprint(Long projectId, Long sprintId) {
		Sprint sprint = sprintDAO.findById(sprintId).get();
		if (sprint.getStatus() == SprintStatus.STARTED || sprint.getStatus() == SprintStatus.CREATED) {
			moveSprintClosedUserStoriesToBacklog(sprint);
			sprint.setStatus(SprintStatus.CLOSED);
		}
	}
	
	private void moveSprintClosedUserStoriesToBacklog(Sprint sprint) {
		Iterator<UserStory> it = sprint.getUserStories().iterator();
		while (it.hasNext()) {
			UserStory us = it.next();
			if (us.getStatus() == UserStoryStatus.OPEN) {
				sprint.getProject().getBacklog().addUserStory(us);
				us.setSprint(null);
			}
		}
		sprint.setUserStories(sprint.getUserStories().stream()
													 .filter(us -> us.getStatus() == UserStoryStatus.CLOSE)
													 .collect(Collectors.toList()));
	}

	@Override
	@Transactional
	public void closeUserStory(Long projectId, Long userStoryId) {
		UserStory us = userStoryDAO.findById(userStoryId).get();
		us.setStatus(UserStoryStatus.CLOSE);
	}

	@Override
	@Transactional
	public void openUserStory(Long projectId, Long userStoryId) {
		UserStory us = userStoryDAO.findById(userStoryId).get();
		us.setStatus(UserStoryStatus.OPEN);
	}

	@Override
	@Transactional
	public Task updateTask(long taskId, Task taskData) {
		Task taskToUpdate = taskDAO.findById(taskId).get();
		taskData.setId(taskToUpdate.getId());
		taskToUpdate = Mapping.mapTo(taskData, Task.class);

		return taskToUpdate;
	}

	@Override
	@Transactional
	public void setTaskStatus(Long taskId, TaskStatus status) {
		Task task = taskDAO.findById(taskId).get();
		task.setStatus(status);
		switch (status) {
		case TODO:
		case DOING:
			taskService.openTaskUserStories(task);
			break;
		case DONE:
			List<UserStory> taskUserStories = task.getTaskUserStories()
												  .stream()
												  .map(UserStoryTasK::getUserStory)
												  .collect(Collectors.toList());
			taskUserStories.forEach(us -> {
				if (userStoryService.isAllUserStoryTasksAreDone(us)) {
					us.setStatus(UserStoryStatus.CLOSE);
				}
			});
			break;
		}
	}
}