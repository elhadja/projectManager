package com.elhadjium.PMBackend.learnHibernate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Person {
	@GeneratedValue
	@Id
	private long id;
	
	@OneToMany(mappedBy = "person", orphanRemoval = true)
	private List<Phon> phones = new ArrayList<Phon>();
	
	public void addPhone(Phon phon) {
		this.phones.add(phon);
		phon.setPerson(this);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Phon> getPhones() {
		return phones;
	}

	public void setPhones(List<Phon> phones) {
		this.phones = phones;
	}
}
