package com.elhadjium.PMBackend.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UserStory {
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private Backlog backlog;
	
	@ManyToOne
	private Sprint sprint;
	
	private String description;
	private String storyPoint;

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

	public String getStoryPoint() {
		return storyPoint;
	}

	public void setStoryPoint(String storyPoint) {
		this.storyPoint = storyPoint;
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