package com.elhadjium.PMBackend.service;

import java.util.List;

import com.elhadjium.PMBackend.entity.User;

public interface UserDAOCustom {
	public List<User> getUsersByCriteria(String pseudo, String firstname, String lastname);
}
