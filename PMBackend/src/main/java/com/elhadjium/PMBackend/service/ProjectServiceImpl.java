package com.elhadjium.PMBackend.service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.UserProject;
import com.elhadjium.PMBackend.dao.ProjectDAO;
import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;
import com.elhadjium.PMBackend.entity.User;

@Service
public class ProjectServiceImpl implements ProjectService {
	@Autowired
	private ProjectDAO projectDao;
	
	@Autowired
	private UserDAO userDAO;

	@Transactional
	public void updateProject(Long projectId, UpdateProjectInputDTO updateProjectInputDTO) {
		updateProjectInputDTO.validate();
		try {
			Project project = projectDao.findById(projectId).get();
			project.setName(updateProjectInputDTO.getProjectName());
			project.setDescription(updateProjectInputDTO.getProjectDescription());

			Map<Long, User> userCache = new HashMap<Long, User>();
			for (UserProject userProject: project.getUsers()) {
				if (!updateProjectInputDTO.getProjectUsersIds().contains(userProject.getUser().getId())) {
					project.removeUser(userProject.getUser());
				}
			}

			for (Long userId: updateProjectInputDTO.getProjectUsersIds()) {
				userCache.put(userId, userDAO.findById(userId).get());
				if (!project.getUsers().contains(new UserProject(userCache.get(userId), project))) {
					project.addUser(userCache.get(userId));
				} 
			}
			
			project.removeAllManagers();
			for (Long managerId: updateProjectInputDTO.getProjectManagersIds()) {
				User user = userCache.containsKey(managerId) ? userCache.get(managerId) : userDAO.findById(managerId).get();
				project.addManager(user);
			}
			
		} catch (NoSuchElementException e) {
			// TODO
		}
	}

}
