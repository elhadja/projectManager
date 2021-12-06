package com.elhadjium.PMBackend.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.elhadjium.PMBackend.entity.TaskTask;
import com.elhadjium.PMBackend.entity.TaskTaskId;

public interface TaskTaskDAO extends JpaRepository<TaskTask, TaskTaskId> {
	@Modifying
	@Transactional
	@Query("delete from TaskTask u where u.dependent.id = :id")
	void deleteHelp(@Param("id") long id);
}
