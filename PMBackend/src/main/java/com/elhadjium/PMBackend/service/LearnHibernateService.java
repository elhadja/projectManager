package com.elhadjium.PMBackend.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elhadjium.PMBackend.common.Mapping;
import com.elhadjium.PMBackend.dao.ProjectDAO;
import com.elhadjium.PMBackend.dao.UserStoryDAO;
import com.elhadjium.PMBackend.dto.AddUserStoryDTO;
import com.elhadjium.PMBackend.entity.Backlog;
import com.elhadjium.PMBackend.entity.UserStory;

@Service
public class LearnHibernateService {
	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private UserStoryDAO userStoryDAO;
	
	@Transactional
	public long addUserStrotyToBacklog(Long projectId, AddUserStoryDTO userStoryDTO) {
		userStoryDTO.validate();
		UserStory us = Mapping.mapTo(userStoryDTO, UserStory.class);
		userStoryDAO.save(us);
		Backlog backlog = projectDAO.findById(projectId).get().getBacklog();
		backlog.addUserStory(us);
		return us.getId();
	}
}