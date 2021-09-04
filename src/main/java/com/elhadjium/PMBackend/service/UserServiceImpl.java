package com.elhadjium.PMBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.entity.User;
import com.elhadjium.PMBackend.exception.PMEntityExistsException;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	UserDAO userDAO;

	@Override
	public Long signup(User user) {
		if (userDAO.findByPseudo(user.getPseudo()) != null || userDAO.findByEmail(user.getEmail()) != null) {
			throw new PMEntityExistsException(user.getEmail() + " or " + user.getPseudo() + "is already used");
		}

		return userDAO.save(user).getId();
	}

}
