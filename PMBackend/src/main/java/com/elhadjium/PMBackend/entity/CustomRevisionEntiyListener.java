package com.elhadjium.PMBackend.entity;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;

public class CustomRevisionEntiyListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
        CustomRevisionEntity customRevisionEntity = (CustomRevisionEntity) revisionEntity;
            customRevisionEntity.setModifiedBy(((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
	}
}
