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

import org.hibernate.SessionFactory;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.UserProject;
import com.elhadjium.PMBackend.common.Mapping;
import com.elhadjium.PMBackend.common.MessageManager;
import com.elhadjium.PMBackend.dao.CustomRevisionEntityDAO;
import com.elhadjium.PMBackend.dao.ProjectDAO;
import com.elhadjium.PMBackend.dao.SprintDAO;
import com.elhadjium.PMBackend.dao.TaskDAO;
import com.elhadjium.PMBackend.dao.TaskTaskDAO;
import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.dao.UserStoryDAO;
import com.elhadjium.PMBackend.dto.AddUserStoryDTO;
import com.elhadjium.PMBackend.dto.UserDTO;
import com.elhadjium.PMBackend.dto.InviteUsersToProjectInputDTO;
import com.elhadjium.PMBackend.dto.StartSprintDTO;
import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;
import com.elhadjium.PMBackend.entity.Backlog;
import com.elhadjium.PMBackend.entity.CustomRevisionEntity;
import com.elhadjium.PMBackend.entity.InvitationToProject;
import com.elhadjium.PMBackend.entity.Sprint;
import com.elhadjium.PMBackend.entity.SprintStatus;
import com.elhadjium.PMBackend.entity.Task;
import com.elhadjium.PMBackend.entity.TaskStatus;
import com.elhadjium.PMBackend.entity.TaskTask;
import com.elhadjium.PMBackend.entity.UserAccount;
import com.elhadjium.PMBackend.entity.UserStory;
import com.elhadjium.PMBackend.entity.UserStoryStatus;
import com.elhadjium.PMBackend.entity.UserStoryTasK;
import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
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
	
	@Autowired
	private TaskTaskDAO taskTaskDao;
	
	@Autowired
	private CustomRevisionEntityDAO customRevisionEntityDAO;
	
	@Autowired
	private MessageManager messageManager;
	
	// TODO integration testing
	@Transactional
	public void updateProject(Long projectId, UpdateProjectInputDTO updateProjectInputDTO) {
		updateProjectInputDTO.validate();
		try {
			Project project = projectDao.findById(projectId).get();
			project.setName(updateProjectInputDTO.getProjectName());
			project.setDescription(updateProjectInputDTO.getProjectDescription());

			// get users to remove from project
			List<UserAccount> usersToRemoveFromProject = new ArrayList<UserAccount>();
			for (UserProject userProject: project.getUsers()) {
				if (!updateProjectInputDTO.getProjectUsersIds().contains(userProject.getUser().getId())) {
					usersToRemoveFromProject.add(userProject.getUser());
				}
			}
			// remove users
			for (UserAccount userToRemove: usersToRemoveFromProject) {
				project.removeUser(userToRemove);
			}

			// add users to project
			Map<Long, UserAccount> userCache = new HashMap<Long, UserAccount>();
			for (Long userId: updateProjectInputDTO.getProjectUsersIds()) {
				userCache.put(userId, userDAO.findById(userId).get());
				if (!project.getUsers().contains(new UserProject(userCache.get(userId), project))) {
					project.addUser(userCache.get(userId));
				} 
			}
			
			// actualize managers
			project.removeAllManagers();
			for (Long managerId: updateProjectInputDTO.getProjectManagersIds()) {
				UserAccount user = userCache.containsKey(managerId) ? userCache.get(managerId) : userDAO.findById(managerId).get();
				project.addManager(user);
			}
			
		} catch (NoSuchElementException e) {
			// TODO
		}
	}

	@Override
	@Transactional
	public void addInvitations(Long projectId, InviteUsersToProjectInputDTO input) {
		UserAccount author = userDAO.findById(input.getAuthorId()).get();
		boolean isManager = false;
		for (Project managedProject: author.getManagedProjects()) {
			if (managedProject.getId() == projectId) {
				isManager = true;
				break;
			}
		}
		if (!isManager) {
			throw new PMRuntimeException(messageManager.getTranslation(MessageManager.NO_RIGHTS_FOR_SENDING_INVITATIONS));
		}

		UserAccount guest = userDAO.findById(input.getGuestId()).get();
		guest.getProjects().forEach((project) -> {
			if (project.getProject().getId() == projectId) {
				throw new PMRuntimeException(messageManager.getTranslation(MessageManager.USER_ALREADY_ASSOCIATED_WITH_PROJECT, author.getId()));
			}
		});
		
		guest.getInvitationnToProject().forEach((invitation) -> {
			if (invitation.getProject().getId() == projectId) {
				throw new PMRuntimeException(messageManager.getTranslation(MessageManager.INVITATION_ALREADY_SENT, guest.getId()));
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
		us.setStatus(UserStoryStatus.OPENED);
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
		us.setStatus(UserStoryStatus.OPENED);
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
		userStoryToUpdate.setImportance(userStoryData.getImportance());

		userStoryDAO.save(userStoryToUpdate);
	}

	@Override
	@Transactional
	public List<UserStory> getBacklogUserStories(Long projectId) {
		List<UserStory> userStories = projectDao.findById(projectId).get().getBacklog().getUserStories();
		userStories.forEach(us -> us.getTasks()); // force loading tasks to avoid lazy initialization exception

		return userStories;
	}

	@Override
	@Transactional
	public List<UserStory> getSprintUserStories(Long parseId, Long sprintId) {
		List<UserStory> userStories = userStoryDAO.findBySprintId(sprintId);
		userStories.forEach(us -> us.getTasks());
		return userStories;
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
	public Long createTask(Task taskData) {
		Task task = Mapping.mapTo(taskData, Task.class);
		task.setStatus(TaskStatus.TODO);
		task.getTaskTaskSet().forEach(taskTask -> {
			taskTask.setDependent(taskDAO.findById(taskTask.getDependent().getId()).get());
		});
		task.getTaskUserStories().forEach(taskUserStory -> {
			taskUserStory.setUserStory(userStoryDAO.findById(taskUserStory.getUserStory().getId()).get());
		});
		taskDAO.save(task);
		
		return task.getId();
	}

	@Deprecated
	@Override
	@Transactional
	public void removeTask(Long userStoryId, Long taskId) {
		Task task = taskDAO.findById(taskId).get();
		task.removeUserStory(userStoryDAO.findById(userStoryId).get());
		taskTaskDao.deleteHelp(taskId);
		taskDAO.delete(task);
	}
	

	@Override
	public void removeTask(Set<Long> taskIds) {
		taskIds.forEach(taskId -> removeTask(taskId));
	}
	
	@Transactional
	private void removeTask(long taskId) {
		Task task = taskDAO.findById(taskId).get();
		//task.removeUserStory(userStoryDAO.findById(userStoryId).get());
		taskTaskDao.deleteHelp(taskId);
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
		//AuditReaderFactory.get()
		List<CustomRevisionEntity> revs = userStoryDAO.getAudit(47L);
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
		if (sprint.getProject().getSprints().stream().anyMatch(s -> s.getStatus().equals(SprintStatus.STARTED))) {
			throw new PMRuntimeException(messageManager.getTranslation(MessageManager.SPRINT_ALREADY_ONGOING));
		}
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
			if (us.getStatus() == UserStoryStatus.OPENED) {
				sprint.getProject().getBacklog().addUserStory(us);
				us.setSprint(null);
			}
		}
		sprint.setUserStories(sprint.getUserStories().stream()
													 .filter(us -> us.getStatus() == UserStoryStatus.CLOSED)
													 .collect(Collectors.toList()));
	}

	@Override
	@Transactional
	public void closeUserStory(Long projectId, Long userStoryId) {
		UserStory us = userStoryDAO.findById(userStoryId).get();
		us.setStatus(UserStoryStatus.CLOSED);
	}

	@Override
	@Transactional
	public void openUserStory(Long projectId, Long userStoryId) {
		UserStory us = userStoryDAO.findById(userStoryId).get();
		us.setStatus(UserStoryStatus.OPENED);
	}

	// TODO test
	@Override
	@Transactional
	public Task updateTask(long taskId, Task taskData) {
		Task taskToUpdate = taskDAO.findById(taskId).get();
		taskToUpdate.setDescription(taskData.getDescription());
		taskToUpdate.setDuration(taskData.getDuration());
		taskToUpdate.setDefinitionOfDone(taskData.getDefinitionOfDone());
		taskToUpdate.setUser(taskData.getUser());
		
		taskToUpdate.removeDependencies(taskToUpdate.getTaskTaskSet()
													.stream()
													.filter(taskTask -> taskData.getTaskTaskSet()
																				.stream()
																				.allMatch(incomingTaskTask -> !incomingTaskTask.getDependent().getId().equals(taskTask.getDependent().getId())))
													.map(TaskTask::getDependent)
													.collect(Collectors.toList()));
		taskData.getTaskTaskSet().forEach(taskTask -> {
			taskToUpdate.addDependency(taskDAO.findById(taskTask.getDependent().getId()).get());
		});
		Iterator<UserStoryTasK> it = taskToUpdate.getTaskUserStories().iterator();
		while (it.hasNext()) {
			UserStory us = it.next().getUserStory();
			if (taskData.getTaskUserStories().stream().allMatch(t -> !t.getUserStory().getId().equals(us.getId()))) {
				taskToUpdate.removeUserStory(us, it);
			}
		}
		taskData.getTaskUserStories().forEach(taskUs -> taskToUpdate.addUserStory(userStoryDAO.findById(taskUs.getUserStory().getId()).get()));
		
		//taskDAO.save(taskToUpdate);

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
					us.setStatus(UserStoryStatus.CLOSED);
				}
			});
			break;
		}
	}

	@Override
	public List<UserAccount> getProjectUsers(long projectId) {
		return projectDao.findById(projectId).get().getUsers()
						.stream()
						.map(UserProject::getUser)
						.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void removeUserFromProject(Long projectId, Long userId) {
		Project project = projectDao.findById(projectId).get();
		UserAccount user = project.getUsers().stream()
											.map(UserProject::getUser)
											.filter(_user -> _user.getId().equals(userId))
											.findAny()
											.orElse(null);
		boolean isUserManager = project.getManagers().contains(user);
		if (project.getManagers().size() == 1 && isUserManager) {
			projectDao.deleteById(projectId);
		} else if (isUserManager) {
			project.removeManager(user);
			project.removeUser(user);
		} else {
			project.removeUser(user);
		}
	}

	@Override
	public List<CustomRevisionEntity> getUserStoryAudit(Long id) {
		return userStoryDAO.getAudit(id);
	}

	@Override
	public List<CustomRevisionEntity> getTaskAudit(Long id) {
		return customRevisionEntityDAO.getTaskActivities(id);
	}

	@Override
	public List<CustomRevisionEntity> getSprintAudit(Long id) {
		return customRevisionEntityDAO.getSprintActivities(id);
	}
}