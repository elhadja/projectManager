package com.elhadjium.PMBackend.entity;

import javax.persistence.Entity;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;


@Entity
@RevisionEntity(CustomRevisionEntiyListener.class)
public class CustomRevisionEntity extends DefaultRevisionEntity {
	private String modifiedBy;
	private String comment;

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
