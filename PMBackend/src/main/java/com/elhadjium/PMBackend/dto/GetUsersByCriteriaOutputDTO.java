package com.elhadjium.PMBackend.dto;

public class GetUsersByCriteriaOutputDTO implements DTOValidator {
	private long id;
	private String firstname;
	private String lastname;
	private String pseudo;
	
	public GetUsersByCriteriaOutputDTO(long id, String firstname, String lastname, String pseudo) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.pseudo = pseudo;
	}
	
	public GetUsersByCriteriaOutputDTO() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	@Override
	public void validate() {
		
	}
}
