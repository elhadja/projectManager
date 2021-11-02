package com.elhadjium.PMBackend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elhadjium.PMBackend.entity.Task;

public interface TaskDAO extends JpaRepository<Task, Long> {

}
