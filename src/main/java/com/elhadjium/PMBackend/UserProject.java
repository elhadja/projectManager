package com.elhadjium.PMBackend;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.elhadjium.PMBackend.entity.UserAccount;

@Entity
public class UserProject implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public UserProject() {
		
	}
	
	public UserProject(UserAccount user, Project project) {
		super();
		this.user = user;
		this.project = project;
	}

	@Id
	@ManyToOne
	private UserAccount user;

	@Id
	@ManyToOne
	private Project project;

	public UserAccount getUser() {
		return user;
	}

	public void setUser(UserAccount user) {
		this.user = user;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(project, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserProject other = (UserProject) obj;
		return Objects.equals(project, other.project) && Objects.equals(user, other.user);
	}
}