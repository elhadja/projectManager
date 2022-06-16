package com.elhadjium.PMBackend.dao;

import java.util.List;

import com.elhadjium.PMBackend.entity.CustomRevisionEntity;

public interface CustomRevisionEntityDAOCustom {
	List<CustomRevisionEntity> getTaskActivities(Long taskId);
	public List<CustomRevisionEntity> getSprintActivities(Long sprintId);
}
