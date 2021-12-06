package com.elhadjium.PMBackend.entity;

import java.io.Serializable;

public class TaskTaskId implements Serializable {
	private static final long serialVersionUID = 1L;

	private Task task;
	private Task dependent;

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Task getDependent() {
		return dependent;
	}

	public void setDependent(Task dependent) {
		this.dependent = dependent;
	}
}
