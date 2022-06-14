package com.elhadjium.PMBackend.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.elhadjium.PMBackend.entity.CustomRevisionEntity;

public class CustomRevisionEntityDAOCustomImpl implements CustomRevisionEntityDAOCustom {
	@Autowired
	private EntityManager em;
	@Override
	public List<CustomRevisionEntity> getTaskActivities(Long taskId) {
		List<CustomRevisionEntity> result = new ArrayList<>();
		String sqlQuery = resourceFileAsString("/sql/getTaskAudit.sql");
		if (sqlQuery != null) {
			result = em.createNativeQuery(sqlQuery, CustomRevisionEntity.class)
				.setParameter("task_id", taskId)
				.getResultList();
		}
		
	
		return result;
	}
	
	//TODO move util file
	private String resourceFileAsString(String path) {
		InputStream is = getClass().getResourceAsStream(path);
		if (is != null) {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader reader = new BufferedReader(isr);
			return reader.lines().collect(Collectors.joining(System.lineSeparator()));
		}
		
		return null;
	}
}
