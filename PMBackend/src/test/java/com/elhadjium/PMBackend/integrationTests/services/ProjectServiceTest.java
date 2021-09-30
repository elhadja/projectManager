package com.elhadjium.PMBackend.integrationTests.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import com.elhadjium.PMBackend.dao.ProjectDAO;
import com.elhadjium.PMBackend.dao.UserDAO;
import com.elhadjium.PMBackend.service.ProjectService;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProjectServiceTest {
	@Autowired
	ProjectService projectService;
	
	@Autowired
	UserDAO testDAO;
	
	@Autowired
	ProjectDAO projectDAO;
	
	@Autowired
	UserDAO userDAO;
	
	@Test
	@Sql("/data.sql")
	public void test() throws Exception {
	}
	
	@Test
	@Sql("/data.sql")
	public void test2() throws Exception {

	}
}
