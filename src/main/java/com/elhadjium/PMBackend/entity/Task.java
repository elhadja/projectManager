package com.elhadjium.PMBackend.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Task implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private User user;
	
	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserStoryTasK> taskUserStories = new HashSet<UserStoryTasK>();
	
	@OneToMany(cascade = CascadeType.ALL)
	private Set<TaskTask> taskTaskSet = new HashSet<TaskTask>();
	
	@Enumerated(EnumType.STRING)
	private TaskStatus status;
	
	@Column(nullable = true)
	private float duration;

	private String description;
	private String definitionOfDone;
	
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
	
	public void addDependency(Task task) {
		if (task != null) {
			TaskTask taskTask = new TaskTask(this, task);
			this.taskTaskSet.add(taskTask);
			task.getTaskTaskSet().add(taskTask);
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

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public String getDefinitionOfDone() {
		return definitionOfDone;
	}

	public void setDefinitionOfDone(String definitionOfDone) {
		this.definitionOfDone = definitionOfDone;
	}

	public Set<TaskTask> getTaskTaskSet() {
		return taskTaskSet;
	}

	public void setTaskTaskSet(Set<TaskTask> taskDepencies) {
		this.taskTaskSet = taskDepencies;
	}
	
	public Set<Task> getDependencies() {
		return taskTaskSet.stream().map(TaskTask::getDependent).collect(Collectors.toSet());
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