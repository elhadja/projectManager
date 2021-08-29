package com.elhadjium.PMBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.entity.User;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	UserDAO userDAO;

	@Override
	public Long signup(User user) {
		return userDAO.save(user).getId();
	}

}
