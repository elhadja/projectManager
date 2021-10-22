package com.elhadjium.PMBackend.learnHibernate;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonDAO extends JpaRepository<Person, Long>{

}
