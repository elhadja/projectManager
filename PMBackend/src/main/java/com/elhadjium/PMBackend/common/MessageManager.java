package com.elhadjium.PMBackend.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageManager {
	public static final String INCORRECT_CREDENTIALS = "msgErrorIncorrectCredentials";
	public static final String PASSWORD_RESET_SUBJECT = "reinitializePasswordSubject";
	public static final String INVALLID_TOKEN = "msgErrorInvalidToken";
	public static final String MAIL_CONFIRMATION_SUBJECT = "mailConfirmationSubject";
	public static final String ENTITY_NOT_FOUND_ERROR = "entityNotFoundError";
	public static final String ENTITY_ALREADY_EXISTS = "msgErrorUserAlreadExists";
	public static final String PROJECT_ALREADY_EXISTS = "msgErrorProjectAlreadyExists";
	public static final String NO_RIGHTS_FOR_SENDING_INVITATIONS = "noRightsForSendingInvitations";
	public static final String USER_ALREADY_ASSOCIATED_WITH_PROJECT = "userAlreadyExists";
	public static final String INVITATION_ALREADY_SENT = "invitationAlreadySentToUse";
	public static final String SPRINT_ALREADY_ONGOING = "sprintAlreadyOnGoing";
	
	@Autowired
	private MessageSource messageSource;

	public String getTranslation(String key) {
		return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
	}
	
	public String getTranslation(String key, Object ...args) {
		return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
	}
	
	public static String getEntityNotFoundDetails(long entityId) {
		return  "The PK with value " + entityId + " not found. Ensure that that value is correct and exists";
	}
}
