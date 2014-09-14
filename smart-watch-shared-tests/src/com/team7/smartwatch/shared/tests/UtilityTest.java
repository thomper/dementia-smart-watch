package com.team7.smartwatch.shared.tests;

import com.team7.smartwatch.shared.Utility;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilityTest {

	@Test
	public void arrayContainsNull_EmptyArray_False() {
		assertFalse(Utility.arrayContainsNull());
	}


	@Test
	public void arrayContainsNull_ArrayWithoutNulls_False() {
		assertFalse(Utility.arrayContainsNull("some text", "Different text", "key:value"));
	}
	
	@Test
	public void arrayContainsNull_ArrayWithNulls_True() {
		assertTrue(Utility.arrayContainsNull("some text", null, "key:value"));
	}

	@Test
	public void arrayContainsNull_ArrayAllNulls_True() {
		assertTrue(Utility.arrayContainsNull(null, null, null, null, null, null));
	}
}
