package com.elhadjium.PMBackend.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elhadjium.PMBackend.entity.User;
import com.elhadjium.PMBackend.util.JavaUtil;

@Repository
public class UserDAOCustomImpl implements UserDAOCustom {
	@Autowired
	EntityManager em;

	@Override
	public List<User> getUsersByCriteria(String pseudo, String firstname, String lastname) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> root = cq.from(User.class);
		
		Predicate p = cb.isNotNull(root.get("id"));

		if (!JavaUtil.isNullOrEmpty(pseudo)) {
			p = cb.and(p, cb.equal(root.get("pseudo"), pseudo));
		}
		
		
		if (!JavaUtil.isNullOrEmpty(firstname)) {
			p = cb.and(p, cb.equal(root.get("firstName"), firstname));
		}

		if (!JavaUtil.isNullOrEmpty(lastname)) {
			p = cb.and(p, cb.equal(root.get("lastName"), lastname));
		}
		
		cq.where(p);
		
		return em.createQuery(cq.select(root)).getResultList();
	}
}
