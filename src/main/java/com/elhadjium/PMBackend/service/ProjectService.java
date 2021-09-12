package com.elhadjium.PMBackend.service;

import com.elhadjium.PMBackend.dto.UpdateProjectInputDTO;

public interface ProjectService {
	public void updateProject(Long projectId, UpdateProjectInputDTO updateProjectInputDTO);
}
