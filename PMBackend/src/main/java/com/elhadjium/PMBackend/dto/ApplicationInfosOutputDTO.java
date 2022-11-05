package com.elhadjium.PMBackend.dto;

public class ApplicationInfosOutputDTO {
	private String name;
	private String environnement;
	private String version;
	
	public ApplicationInfosOutputDTO(String environnement, String version, String name) {
		this.environnement = environnement;
		this.version = version;
		this.name = name;
	}

	public String getEnvironnement() {
		return environnement;
	}

	public void setEnvironnement(String environnemtnt) {
		this.environnement = environnemtnt;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
