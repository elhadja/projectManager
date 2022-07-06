package com.elhadjium.PMBackend.dto;

public class LoginOutputDTO {
	private Long id;
	private String token;
	private Long expires;
	
	public LoginOutputDTO(Long id, String token) {
		super();
		this.id = id;
		this.token = token;
	}
	
	public LoginOutputDTO(Long id, String token, Long expires) {
		super();
		this.id = id;
		this.token = token;
		this.expires = expires;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getExpires() {
		return expires;
	}

	public void setExpires(Long expires) {
		this.expires = expires;
	}
}
