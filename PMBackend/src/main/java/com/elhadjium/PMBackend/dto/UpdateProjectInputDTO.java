package com.elhadjium.PMBackend.dto;

import java.util.List;

import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.util.JavaUtil;

public class UpdateProjectInputDTO implements DTOValidator {
	private String projectName;
	private String projectDescription;
	private List<Long> projectManagersIds;
	private List<Long> projectUsersIds;
	
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

	public List<Long> getProjectManagersIds() {
		return projectManagersIds;
	}

	public void setProjectManagersIds(List<Long> projectManagersIds) {
		this.projectManagersIds = projectManagersIds;
	}

	public List<Long> getProjectUsersIds() {
		return projectUsersIds;
	}

	public void setProjectUsersIds(List<Long> projectUsersIds) {
		this.projectUsersIds = projectUsersIds;
	}

	@Override
	public void validate() {
		if (JavaUtil.isNullOrEmpty(projectName)) {
			// TODO to internaitionalize
			throw new PMInvalidInputDTO("project name cannot be null or empty");
		}
		
		for (Long id: projectManagersIds) {
			if (id <= 0) {
				throw new PMInvalidInputDTO("project manager's IDs must be >= 0");
			}
		}
		
		for (Long id: projectUsersIds) {
			if (id <= 0) {
				throw new PMInvalidInputDTO("project user's IDs must be >= 0");
			}
		}
	}
}