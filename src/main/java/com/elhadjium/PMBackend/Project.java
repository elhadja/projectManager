package com.elhadjium.PMBackend;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.elhadjium.PMBackend.entity.User;

@Entity
public class Project implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	private String name;
	private String description;

	@OneToMany(mappedBy = "project",
			   cascade = CascadeType.ALL,
			   orphanRemoval = true)
	private List<UserProject> users = new ArrayList<UserProject>();
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<User> managers = new ArrayList<User>();
	
	public void addUser(User user) {
		UserProject userProject = new UserProject();
		userProject.setProject(this);
		userProject.setUser(user);
		
		users.add(userProject);
		user.getProjects().add(userProject);
	}
	
	public void removeUser(User user)  {
		throw new RuntimeException("not implemented yet");
	}
	
	public void addManager(User manager) {
		managers.add(manager);
		manager.getManagedProjects().add(this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<UserProject> getUsers() {
		return users;
	}

	public void setUsers(List<UserProject> users) {
		this.users = users;
	}
	
	public List<User> getManagers() {
		return managers;
	}

	public void setManagers(List<User> managers) {
		this.managers = managers;
	}

	@Override
	public int hashCode() {
		return Objects.hash(description, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		return Objects.equals(description, other.description) && Objects.equals(name, other.name);
	}
	
	
}
