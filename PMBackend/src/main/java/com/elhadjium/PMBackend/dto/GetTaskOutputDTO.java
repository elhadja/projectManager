package com.elhadjium.PMBackend.dto;

import java.util.Set;

import com.elhadjium.PMBackend.entity.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class GetTaskOutputDTO {
	private long id;
	private String userPseudo;
	private String description;
	private float duration;
	private String definitionOfDone;
	private TaskStatus status;
	private Set<TaskDepencieOutputDTO> dependencies;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserPseudo() {
		return userPseudo;
	}

	public void setUserPseudo(String userPseudo) {
		this.userPseudo = userPseudo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public String getDefinitionOfDone() {
		return definitionOfDone;
	}

	public void setDefinitionOfDone(String definitionOfDone) {
		this.definitionOfDone = definitionOfDone;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public Set<TaskDepencieOutputDTO> getDependencies() {
		return dependencies;
	}

	public void setDependencies(Set<TaskDepencieOutputDTO> taskDependencies) {
		this.dependencies = taskDependencies;
	}
}
