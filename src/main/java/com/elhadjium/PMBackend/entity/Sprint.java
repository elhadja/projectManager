package com.elhadjium.PMBackend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.elhadjium.PMBackend.Project;

@Entity
public class Sprint {
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	@ManyToOne
	private Project project;
	
	// TODO think about us removal
	@OneToMany(mappedBy = "sprint", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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
	
	public void addUserStory(UserStory us) {
		userStories.add(us);
		us.setSprint(this);
	}
	
	public void removeUserStory(UserStory us) {
		userStories.remove(us);
		us.setSprint(null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		Sprint other = (Sprint) obj;
		return Objects.equals(id, other.id);
	}
}
