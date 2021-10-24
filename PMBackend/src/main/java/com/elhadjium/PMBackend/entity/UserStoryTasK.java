package com.elhadjium.PMBackend.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UserStoryTasK implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne
	private UserStory userStory;
	
	@Id
	@ManyToOne
	private Task task;

	public UserStoryTasK(UserStory userStory, Task task) {
		super();
		this.userStory = userStory;
		this.task = task;
	}
	
	public UserStoryTasK() {
		
	}

	public UserStory getUserStory() {
		return userStory;
	}

	public void setUserStory(UserStory userStory) {
		this.userStory = userStory;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@Override
	public int hashCode() {
		return Objects.hash(task, userStory);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserStoryTasK other = (UserStoryTasK) obj;
		return Objects.equals(task, other.task) && Objects.equals(userStory, other.userStory);
	}
}
