package com.elhadjium.PMBackend.entity;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.elhadjium.PMBackend.Project;

@Entity()
public class InvitationToProject {
	// TODO make composite primary key
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private User guest;

	@ManyToOne
	private User author;
	
	@ManyToOne
	private Project project;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getGuest() {
		return guest;
	}

	public void setGuest(User guest) {
		this.guest = guest;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	@Override
	public int hashCode() {
		return Objects.hash(author, guest, project);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InvitationToProject other = (InvitationToProject) obj;
		return Objects.equals(author, other.author) && Objects.equals(guest, other.guest)
				&& Objects.equals(project, other.project);
	}
}
