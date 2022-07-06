package com.elhadjium.PMBackend.util;

public class DateTimeUtil {
	public static Long minitesToMillisecondes(Integer minites) {
		return minites != null && minites > 0 ? 1000 * 60 * minites.longValue() : 0;
	}
	
	public static Long hourToMillisecondes(Integer hour) {
		return hour != null && hour > 0 ? minitesToMillisecondes(hour * 60) : 0;
	}
}
