package com.elhadjium.PMBackend.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.entity.CustomUserDetailsImpl;
import com.elhadjium.PMBackend.entity.User;
import com.elhadjium.PMBackend.exception.PMEntityExistsException;

@Service
public class UserServiceImpl implements UserService{
	@Autowired
	UserDAO userDAO;
	
	private BCryptPasswordEncoder passwordEncodere = new BCryptPasswordEncoder();

	@Override
	public Long signup(User user) {
		if (userDAO.findByPseudo(user.getPseudo()) != null || userDAO.findByEmail(user.getEmail()) != null) {
			throw new PMEntityExistsException(user.getEmail() + " or " + user.getPseudo() + "is already used");
		}

		user.setPassword(passwordEncodere.encode(user.getPassword()));
		return userDAO.save(user).getId();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User u = userDAO.findByEmail(username);
			return new CustomUserDetailsImpl(u.getEmail(), u.getPassword(), u.getId(), (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>());
		} catch (Exception e) {
			throw new UsernameNotFoundException("user not found");
		}
	}
}
