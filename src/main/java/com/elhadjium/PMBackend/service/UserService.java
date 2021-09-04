package com.elhadjium.PMBackend.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.elhadjium.PMBackend.entity.User;

public interface UserService extends UserDetailsService {
	public Long signup(User user);
}
