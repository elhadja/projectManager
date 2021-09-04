package com.elhadjium.PMBackend.entity;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetails extends UserDetails {
	public long getUserId();
}
