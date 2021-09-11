package com.elhadjium.PMBackend.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.elhadjium.PMBackend.Project;
import com.elhadjium.PMBackend.UserProject;

@Entity
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	private String firstName;
	private String lastName;
	@Column(unique = true)
	private String email;
	@Column(unique = true)
	private String pseudo;
	private String password;
	
	@OneToMany(mappedBy = "user",
			   cascade = CascadeType.ALL,
			   orphanRemoval = true)
	private List<UserProject> projects = new ArrayList<UserProject>();
	
	@ManyToMany(mappedBy = "managers")
	private List<Project> managedProjects = new ArrayList<Project>();
	
	public User() {}
	
	public User(Long id, String firstName, String lastName, String email, String pseudo, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.pseudo = pseudo;
		this.password = password;
	}
	
	public void addProject(Project project) {
		UserProject userProject = new UserProject();
		userProject.setProject(project);
		userProject.setUser(this);
		
		projects.add(userProject);
		project.getUsers().add(userProject);
	}
	
	public void removeProject(Project userProject) {
		throw new RuntimeException("Not implemented yet");
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<UserProject> getProjects() {
		return projects;
	}

	public void setProjects(List<UserProject> projects) {
		this.projects = projects;
	}
	
	public List<Project> getManagedProjects() {
		return managedProjects;
	}

	public void setManagedProjects(List<Project> managedProjects) {
		this.managedProjects = managedProjects;
	}

	@Override
	public int hashCode() {
		return Objects.hash(email, pseudo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(email, other.email) && Objects.equals(pseudo, other.pseudo);
	}
}
