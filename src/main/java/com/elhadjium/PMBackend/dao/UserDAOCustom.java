package com.elhadjium.PMBackend.dao;

import java.util.List;

import com.elhadjium.PMBackend.entity.UserAccount;

public interface UserDAOCustom {
	public List<UserAccount> getUsersByCriteria(String pseudo, String firstname, String lastname);
}
