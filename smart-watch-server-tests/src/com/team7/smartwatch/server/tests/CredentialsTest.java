package com.team7.smartwatch.server.tests;

import com.team7.smartwatch.server.Credentials;

import static org.junit.Assert.*;

import org.junit.Test;

public class CredentialsTest {

	@Test
	public void testPasswordIsCorrect_MatchingHash_true() {
		Credentials credentials = new Credentials();
		credentials.password = "password";
		String storedHash = "d31ca2b572992e2ff5335a3a6ada16f64e1f1b7f3999462f8058819b9a7da075";
		String salt = "b0973ae4-1640-4c4e-b350-d38d10bb865a";
		assertTrue(credentials.passwordIsCorrect(salt, storedHash));
	}
}
