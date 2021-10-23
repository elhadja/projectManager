package com.elhadjium.PMBackend.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class UserStory {
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private Backlog backlog;
	
	@ManyToOne
	private Sprint sprint;
	
	@OneToMany(mappedBy = "userStory" , cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Task> tasks = new HashSet<Task>();
	
	private String summary;
	private String description;
	private Long storyPoint;
	
	public void addTask(Task task) {
		this.tasks.add(task);
		task.setUserStory(this);
	}
	
	public void removeTask(Task task) {
		this.tasks.remove(task);
		task.setUserStory(null);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Backlog getBacklog() {
		return backlog;
	}

	public void setBacklog(Backlog backlog) {
		this.backlog = backlog;
	}

	public Sprint getSprint() {
		return sprint;
	}

	public void setSprint(Sprint sprint) {
		this.sprint = sprint;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getStoryPoint() {
		return storyPoint;
	}

	public void setStoryPoint(Long storyPoint) {
		this.storyPoint = storyPoint;
	}
	
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	// TODO think about comparing entities
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserStory other = (UserStory) obj;
		return Objects.equals(id, other.id);
	}
}