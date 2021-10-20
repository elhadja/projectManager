package com.elhadjium.PMBackend.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elhadjium.PMBackend.entity.User;

public interface UserDAO extends JpaRepository<User, Long>, UserDAOCustom {
	User findByEmail(String email);
	User findByPseudo(String pseudo);
	List<User> findByPseudoOrFirstNameOrLastName(String pseudo, String firstName, String lastName);
}
