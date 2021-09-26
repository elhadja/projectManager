package com.elhadjium.PMBackend.dto;

public class GetUsersByCriteriaInputDTO implements DTOValidator {
	private String firstname;
	private String lastname;
	private String pseudo;
	
	public GetUsersByCriteriaInputDTO(String firstname, String lastname, String pseudo) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.pseudo = pseudo;
	}
	
	public GetUsersByCriteriaInputDTO() {
		
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
		// nothing to do
	}
}
