package com.elhadjium.PMBackend.util;

import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;

public class JavaUtil {
	public static boolean isNullOrEmpty(String string) {
		return string == null || string.isEmpty();
	}
	
	public static Long parseId(String id) {
		Long result = null;
		try {
			 result = Long.valueOf(id);
			 if (result <= 0) {
				 throw new NumberFormatException();
			 }
		} catch (NumberFormatException e) {
			// TODO Internationalize
			throw new PMInvalidInputDTO("Invalid Object identifier: " + id + ". Object Id should be greater than 0");
		}
		
		return result;
	}
}
