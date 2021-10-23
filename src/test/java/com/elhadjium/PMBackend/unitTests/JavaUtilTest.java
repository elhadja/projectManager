package com.elhadjium.PMBackend.unitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.elhadjium.PMBackend.exception.PMInvalidInputDTO;
import com.elhadjium.PMBackend.util.JavaUtil;


public class JavaUtilTest {
	
	@ParameterizedTest
	@ValueSource(strings = {"1", "" + Long.MAX_VALUE})
	public void testParseId_shouldBeOk(String id) throws Exception {
		assertEquals(1L, JavaUtil.parseId("1"));
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"-1", "0", "" + Long.MIN_VALUE})
	public void testParseId_shouldThrowException(String id) throws Exception {
		try {
			JavaUtil.parseId(id);
			fail();
		} catch (PMInvalidInputDTO e) {
			// then
		}
	}

}
