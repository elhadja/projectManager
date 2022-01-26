package com.elhadjium.PMBackend.controller;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elhadjium.PMBackend.learnHibernate.Person;
import com.elhadjium.PMBackend.learnHibernate.PersonDAO;
import com.elhadjium.PMBackend.learnHibernate.Phon;
import com.elhadjium.PMBackend.learnHibernate.PhonDAO;

@RestController
@RequestMapping("hibernate")
public class LearnHibernateController {
	@Autowired
	private PersonDAO personDAO;
	
	@Autowired
	private PhonDAO phonDAO;

	/* ************************************** create *********************************** */
	// save collection
	@PostMapping("person/{id-person}/phon")
	public long addPhone(@PathVariable("id-person") String idPerson) {
		Person person = personDAO.findById(getLong(idPerson)).get();
		Phon phon = new Phon();
		person.addPhone(phon);
		personDAO.save(person);
		return phon.getId();
	}
	
	// save phone + save collection
	@Transactional
	@PostMapping("person2/{id-person}/phon")
	public long addPhone2(@PathVariable("id-person") String idPerson) {
		Person person = personDAO.findById(getLong(idPerson)).get();
		Phon phon = new Phon();
		//phonDAO.save(phon);
		person.addPhone(new Phon());
		personDAO.save(person);
		return phon.getId();
	}
	
	/* ********************************** delete **************************************** */
	
	
	@DeleteMapping("delete/phon/{phone-id}")
	public void deletePhone(@PathVariable("phone-id") String phoneId) {
		phonDAO.deleteById(getLong(phoneId));
	}
	
	private Long getLong(String string) {
		return Long.valueOf(string);
	}
	
	/* ******************************** cascade type ******************************** */
	@PostMapping("person")
	public void getPerson() {
		Person person = personDAO.findById(1L).get();
		person.getPhones().get(0).setPerson(null);
		person.getPhones().remove(0);
		personDAO.save(person);
	}
	
}
