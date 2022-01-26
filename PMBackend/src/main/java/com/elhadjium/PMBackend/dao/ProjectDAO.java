package com.elhadjium.PMBackend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elhadjium.PMBackend.Project;

public interface ProjectDAO extends JpaRepository<Project, Long>{
	public Project findByName(String name);
}
