package com.elhadjium.PMBackend.common;

import org.modelmapper.ModelMapper;

public class Mapping {
	public static <T,U> U mapTo(T entitySource, Class<?> targetClass) {
		ModelMapper mp = new ModelMapper();
		return (U) mp.map(entitySource, targetClass);
	}
}
