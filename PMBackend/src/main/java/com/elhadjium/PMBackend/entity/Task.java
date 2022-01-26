package com.elhadjium.PMBackend.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Task implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private User user;
	
	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserStoryTasK> taskUserStories = new HashSet<UserStoryTasK>();
	
	private String description;
	private float duration;
	
	public void addUserStory(UserStory us) {
		UserStoryTasK usTask = new UserStoryTasK(us, this);
		this.taskUserStories.add(usTask);
		us.getUserStoryTasks().add(usTask);
	}
	
	public void removeUserStory(UserStory us) {
		UserStoryTasK usTask = new UserStoryTasK(us, this);
		this.taskUserStories.remove(usTask);
		us.getUserStoryTasks().remove(usTask);
		usTask.setTask(null);
		usTask.setUserStory(null);
	}
	
	public void removeAllUserStory() {
		Iterator<UserStoryTasK> it = taskUserStories.iterator();
		while (it.hasNext()) {
			removeUserStory(it.next().getUserStory());
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<UserStoryTasK> getTaskUserStories() {
		return taskUserStories;
	}

	public void setTaskUserStories(Set<UserStoryTasK> taskUserStories) {
		this.taskUserStories = taskUserStories;
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

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		return Objects.equals(id, other.id);
	}
}