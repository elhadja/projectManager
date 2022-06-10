package com.elhadjium.PMBackend.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elhadjium.PMBackend.entity.CustomRevisionEntity;
import com.elhadjium.PMBackend.entity.UserStory;

public interface UserStoryDAOCustom {
	public List<CustomRevisionEntity> getAudit(Long usId);
}
