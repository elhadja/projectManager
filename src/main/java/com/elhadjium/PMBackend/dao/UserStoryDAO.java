package com.elhadjium.PMBackend.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elhadjium.PMBackend.entity.UserStory;

public interface UserStoryDAO extends JpaRepository<UserStory, Long> {
	List<UserStory> findBySprintId(Long sprintId);
}
