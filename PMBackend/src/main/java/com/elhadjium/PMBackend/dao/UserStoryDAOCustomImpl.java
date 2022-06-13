package com.elhadjium.PMBackend.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.elhadjium.PMBackend.entity.CustomRevisionEntity;
import com.elhadjium.PMBackend.service.contant.MessageConstant;


@Repository
public class UserStoryDAOCustomImpl implements UserStoryDAOCustom {
	@Autowired
	private EntityManager em;
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	public List<CustomRevisionEntity> getAudit(Long usId) {
		String sqlQuery = "select _rev.id, _rev.timestamp, _rev.modified_by, case"
							+ "	when revtype = 0 then '" + messageSource.getMessage(MessageConstant.H_USER_STORY_CREATED, null, LocaleContextHolder.getLocale()) + "'"
							+ " when revtype = 1 and backlog_mod = 1 and backlog_id is not  null then '" + messageSource.getMessage(MessageConstant.h_USER_STORY_MOVED_TO_BACKLOG, null, LocaleContextHolder.getLocale()) + "'"
							+ "	when revtype = 1 and status_mod = 1 and status = 'OPENED' then '" + messageSource.getMessage(MessageConstant.H_USER_STORY_OPENED, null, LocaleContextHolder.getLocale()) + "'"
							+ " when revtype = 1 and status_mod = 1 and status = 'CLOSED' then '" + messageSource.getMessage(MessageConstant.H_USER_STORY_CLOSED, null, LocaleContextHolder.getLocale()) + "'"
							+ " when revtype = 1 and sprint_mod = 1 then concat('" + messageSource.getMessage(MessageConstant.H_USER_STORY_MOVED_TO_SPRINT, null, LocaleContextHolder.getLocale()) + " ', sprint_id)"
						+ " end as comment"
						+ " from user_story_aud _us_aud"
						+ " inner join custom_revision_entity _rev"
						+ " on _us_aud.rev = _rev.id"
						+ " where _us_aud.id = :id";
		
		return em.createNativeQuery(sqlQuery, CustomRevisionEntity.class)
				.setParameter("id", usId)
				.getResultList();
	}
}
