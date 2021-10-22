package com.elhadjium.PMBackend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elhadjium.PMBackend.entity.UserStory;

public interface UserStoryDAO extends JpaRepository<UserStory, Long> {

}
