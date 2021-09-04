package com.elhadjium.PMBackend.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elhadjium.PMBackend.entity.User;

public interface UserDAO extends JpaRepository<User, Long>{
	User findByEmail(String email);
	User findByPseudo(String pseudo);
}
