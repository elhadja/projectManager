package com.elhadjium.PMBackend.dto;

import java.util.List;

public class GetUserProjectOutputDTO {
	private Long projectId;
	private String projectName;
	private String projectDescription;
	private List<ProjectManagerOutputDTO> projectManagers;
	private List<ProjectManagerOutputDTO> projectUsers;

	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

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

	public List<ProjectManagerOutputDTO> getProjectManagers() {
		return projectManagers;
	}

	public void setProjectManagers(List<ProjectManagerOutputDTO> projectManagers) {
		this.projectManagers = projectManagers;
	}

	public List<ProjectManagerOutputDTO> getProjectUsers() {
		return projectUsers;
	}

	public void setProjectUsers(List<ProjectManagerOutputDTO> projectUsers) {
		this.projectUsers = projectUsers;
	}
}