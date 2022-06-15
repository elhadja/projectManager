package com.elhadjium.PMBackend.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.elhadjium.PMBackend.common.PMConstants;
import com.elhadjium.PMBackend.entity.CustomRevisionEntity;
import com.elhadjium.PMBackend.entity.TaskStatus;
import com.elhadjium.PMBackend.service.contant.MessageConstant;

public class CustomRevisionEntityDAOCustomImpl implements CustomRevisionEntityDAOCustom {
	@Autowired
	private EntityManager em;
	
	@Autowired
	private MessageSource messageSource;

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomRevisionEntity> getTaskActivities(Long taskId) {
		List<CustomRevisionEntity> result = new ArrayList<>();
		String sqlQuery = resourceFileAsString("/sql/getTaskAudit.sql");
		if (sqlQuery != null) {
			result = em.createNativeQuery(sqlQuery, CustomRevisionEntity.class)
				.setParameter("created_comment", messageSource.getMessage(MessageConstant.H_TASK_CREATED, null, LocaleContextHolder.getLocale()))
				.setParameter("status_todo", TaskStatus.TODO.toString())
				.setParameter("status_todo_comment", messageSource.getMessage(MessageConstant.H_TASK_TODO, null, LocaleContextHolder.getLocale()))
				.setParameter("status_doing", TaskStatus.DOING.toString())
				.setParameter("status_doing_comment", messageSource.getMessage(MessageConstant.H_TASK_DOING, null, LocaleContextHolder.getLocale()))
				.setParameter("status_done", TaskStatus.DONE.toString())
				.setParameter("status_done_comment", messageSource.getMessage(MessageConstant.H_TASK_DONE, null, LocaleContextHolder.getLocale()))
				.setParameter("new_us_comment", messageSource.getMessage(MessageConstant.H_TASK_NEW_US_COMMENT, null, LocaleContextHolder.getLocale()))
				.setParameter("removed_us_comment", messageSource.getMessage(MessageConstant.H_TASK_REMOVED_US_COMMENT, null, LocaleContextHolder.getLocale()))
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
