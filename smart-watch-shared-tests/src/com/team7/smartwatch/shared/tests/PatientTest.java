package com.team7.smartwatch.shared.tests;

import com.team7.smartwatch.shared.Patient;

import static org.junit.Assert.*;

import org.junit.Test;

public class PatientTest {

	@Test
	public void fullName_BothNames_True() {

		Patient patient = new Patient();
		patient.firstName = "Jimmy";
		patient.lastName = "Chimichanga";
		
		String expected = "Jimmy Chimichanga";
		assertTrue(expected.equals(patient.fullName()));
	}

	@Test
	public void fullName_firstNameNull_True() {

		Patient patient = new Patient();
		patient.firstName = null;
		patient.lastName = "Chimichanga";
		
		String expected = "Chimichanga";
		assertTrue(expected.equals(patient.fullName()));
	}

	@Test
	public void fullName_firstNameEmpty_True() {

		Patient patient = new Patient();
		patient.firstName = "";
		patient.lastName = "Chimichanga";
		
		String expected = "Chimichanga";
		assertTrue(expected.equals(patient.fullName()));
	}

	@Test
	public void fullName_lastNameNull_True() {

		Patient patient = new Patient();
		patient.firstName = "Jimmy";
		patient.lastName = null;
		
		String expected = "Jimmy";
		assertTrue(expected.equals(patient.fullName()));
	}

	@Test
	public void fullName_lastNameEmpty_True() {

		Patient patient = new Patient();
		patient.firstName = "Jimmy";
		patient.lastName = "";
		
		String expected = "Jimmy";
		assertTrue(expected.equals(patient.fullName()));
	}
}
