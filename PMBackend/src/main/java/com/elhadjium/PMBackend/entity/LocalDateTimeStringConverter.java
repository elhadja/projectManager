package com.elhadjium.PMBackend.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import javax.persistence.Embeddable;

import com.elhadjium.PMBackend.util.JavaUtil;

@Converter
public class LocalDateTimeStringConverter implements AttributeConverter<String, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(String attribute) {
		//return LocalDateTime.parse(attribute);
		return !JavaUtil.isNullOrEmpty(attribute) ? Timestamp.valueOf(attribute) : null;
	}

	@Override
	public String convertToEntityAttribute(Timestamp dbData) {
		return dbData != null ? dbData.toString() : null;
	}
	
}
