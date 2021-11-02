package com.elhadjium.PMBackend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elhadjium.PMBackend.entity.Sprint;

public interface SprintDAO extends JpaRepository<Sprint, Long> {

}