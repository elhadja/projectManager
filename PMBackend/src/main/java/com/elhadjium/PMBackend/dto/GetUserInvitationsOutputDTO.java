package com.elhadjium.PMBackend.dto;

public class GetUserInvitationsOutputDTO {
	private Long invitationToProjectId;
	private String projectName;
	private String projectDescription;
	private String authorPseudo;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getAuthorPseudo() {
		return authorPseudo;
	}

	public void setAuthorPseudo(String authorpseudo) {
		this.authorPseudo = authorpseudo;
	}

	public Long getInvitationToProjectId() {
		return invitationToProjectId;
	}

	public void setInvitationToProjectId(Long invitationToProjectId) {
		this.invitationToProjectId = invitationToProjectId;
	}
}
