package com.elhadjium.PMBackend;

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
import javax.persistence.OneToOne;

import com.elhadjium.PMBackend.entity.Backlog;
import com.elhadjium.PMBackend.entity.InvitationToProject;
import com.elhadjium.PMBackend.entity.Sprint;
import com.elhadjium.PMBackend.entity.UserAccount;

@Entity
public class Project implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true)
	private String name;
	private String description;

	@OneToMany(mappedBy = "project",
			   cascade = CascadeType.ALL,
			   orphanRemoval = true)
	private List<UserProject> users = new ArrayList<UserProject>();
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<UserAccount> managers = new ArrayList<UserAccount>();
	
	@OneToMany(mappedBy = "project")
	private List<InvitationToProject> invitationsToProject = new ArrayList<InvitationToProject>();
	
	@OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private Backlog backlog;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Sprint> sprints = new ArrayList<>();
	
	public void addUser(UserAccount user) {
		UserProject userProject = new UserProject();
		userProject.setProject(this);
		userProject.setUser(user);
		
		users.add(userProject);
		user.getProjects().add(userProject);
	}
	
	public void removeUser(UserAccount user)  {
		UserProject userProject = new UserProject();
		userProject.setUser(user);
		userProject.setProject(this);

		users.remove(userProject);
		user.getProjects().remove(userProject);
	}
	
	public void removeAllUsers() {
		for (UserProject userProject: users) {
			userProject.getUser().getProjects().remove(userProject);
		}

		users.clear();
	}
	
	public void removeAllManagers() {
		for (UserAccount user: managers) {
			user.getManagedProjects().remove(this);
		}

		managers.clear();
	}
	
	public void addManager(UserAccount manager) {
		managers.add(manager);
		manager.getManagedProjects().add(this);
	}
	
	public void removeManager(UserAccount manager) {
		managers.remove(manager);
		manager.getManagedProjects().remove(this);
	}
	
	public void removeInvitation(InvitationToProject invitation) {
		invitationsToProject.remove(invitation);
		invitation.setProject(null);
	}
	
	public void addInvitation(InvitationToProject invitation) {
		invitationsToProject.add(invitation);
		invitation.setProject(this);
	}
	
	public void addSprint(Sprint sprint) {
		this.sprints.add(sprint);
		sprint.setProject(this);
	}
	
	public void removeSprint(Sprint sprint) {
		this.sprints.remove(sprint);
		sprint.setProject(null);
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
	
	public List<UserAccount> getManagers() {
		return managers;
	}

	public void setManagers(List<UserAccount> managers) {
		this.managers = managers;
	}

	public List<InvitationToProject> getInvitationsToProject() {
		return invitationsToProject;
	}

	public void setInvitationsToProject(List<InvitationToProject> invitationsToProject) {
		this.invitationsToProject = invitationsToProject;
	}
	
	public Backlog getBacklog() {
		return backlog;
	}

	public void setBacklog(Backlog backlog) {
		this.backlog = backlog;
	}
	
	public List<Sprint> getSprints() {
		return sprints;
	}

	public void setSprints(List<Sprint> sprints) {
		this.sprints = sprints;
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
