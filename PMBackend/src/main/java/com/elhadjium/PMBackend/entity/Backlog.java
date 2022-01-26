package com.elhadjium.PMBackend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.elhadjium.PMBackend.Project;

@Entity
public class Backlog {
	@Id
	@GeneratedValue
	private Long id;
	
	@OneToOne
	private Project project;
	
	@OneToMany(mappedBy = "backlog",
				cascade = CascadeType.ALL,
				fetch = FetchType.EAGER)
	private List<UserStory> userStories = new ArrayList<UserStory>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public List<UserStory> getUserStories() {
		return userStories;
	}

	public void setUserStories(List<UserStory> userStories) {
		this.userStories = userStories;
	}
	
	public void addUserStory(UserStory userStory) {
		this.userStories.add(userStory);
		userStory.setBacklog(this);
	}
	
	public void deleteUserStory(UserStory userStory) {
		this.userStories.remove(userStory);
		userStory.setBacklog(null);
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
		Backlog other = (Backlog) obj;
		return Objects.equals(id, other.id);
	}
}
