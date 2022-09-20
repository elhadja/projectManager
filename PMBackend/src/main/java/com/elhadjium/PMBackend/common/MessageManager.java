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
	
	@Autowired
	private MessageSource messageSource;

	public String getTranslation(String key) {
		return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
	}
	
	public String getTranslation(String key, Object ...args) {
		return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
	}
}
