package com.elhadjium.PMBackend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.envers.AuditMappedBy;
import org.hibernate.envers.Audited;

import com.elhadjium.PMBackend.Project;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "project_id"})})
public class Sprint {
	@Id
	@GeneratedValue
	private Long id;
	
	@Basic(optional = false)
	private String name;
	
	@Audited
	@Enumerated(EnumType.STRING)
	private SprintStatus status;
	
	@Audited
	@Convert(converter = LocalDateTimeStringConverter.class)
	private String startDate;
	
	@Audited
	@Convert(converter = LocalDateTimeStringConverter.class)
	private String endDate;
	
	@ManyToOne(optional = false)
	private Project project;
	
	@AuditMappedBy(mappedBy = "sprint")
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
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public SprintStatus getStatus() {
		return status;
	}

	public void setStatus(SprintStatus status) {
		this.status = status;
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
