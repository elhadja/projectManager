package com.elhadjium.PMBackend.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class UserStory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private Backlog backlog;
	
	@ManyToOne
	private Sprint sprint;
	
	@OneToMany(mappedBy = "userStory" , cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserStoryTasK> userStoryTasks = new HashSet<UserStoryTasK>();
	
	@Column(nullable = false)
	private String summary;
	private String description;
	private Long storyPoint;
	
	public UserStory() {
		
	}
	
	public UserStory(String summary) {
		super();
		this.summary = summary;
	}

	public void addTask(Task task) {
		UserStoryTasK usTask = new UserStoryTasK(this, task);
		this.userStoryTasks.add(usTask);
		task.getTaskUserStories().add(usTask);
	}
	
	public void removeTask(Task task) {
		UserStoryTasK usTask = new UserStoryTasK(this, task);
		this.userStoryTasks.remove(usTask);
		task.getTaskUserStories().remove(usTask);
		usTask.setTask(null);
		usTask.setUserStory(null);
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
	
	public Set<UserStoryTasK> getUserStoryTasks() {
		return userStoryTasks;
	}

	public void setUserStoryTasks(Set<UserStoryTasK> userStoryTasks) {
		this.userStoryTasks = userStoryTasks;
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