package com.elhadjium.PMBackend.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.elhadjium.PMBackend.entity.UserAccount;

public interface UserDAO extends JpaRepository<UserAccount, Long>, UserDAOCustom {
	UserAccount findByEmail(String email);
	UserAccount findByPseudo(String pseudo);
	List<UserAccount> findByPseudoOrFirstNameOrLastName(String pseudo, String firstName, String lastName);
}
