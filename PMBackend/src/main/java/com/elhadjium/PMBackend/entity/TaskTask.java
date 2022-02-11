package com.elhadjium.PMBackend.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.NamedQueries;

@Entity
@IdClass(TaskTaskId.class)
public class TaskTask implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	private Task task;
	
	@Id
	@ManyToOne
	private Task dependent;
	
	public TaskTask() {
	}
	
	public TaskTask(Task task, Task dependent) {
		this.task = task;
		this.dependent = dependent;
	}

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

	@Override
	public int hashCode() {
		return Objects.hash(dependent, task);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskTask other = (TaskTask) obj;
		return Objects.equals(dependent, other.dependent) && Objects.equals(task, other.task);
	}
}
