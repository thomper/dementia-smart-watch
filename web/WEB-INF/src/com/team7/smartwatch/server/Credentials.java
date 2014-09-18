package com.team7.smartwatch.server;

import java.security.NoSuchAlgorithmException;

import com.team7.smartwatch.shared.Utility;

public class Credentials {
	public String username;
	public String password;

	public boolean passwordIsCorrect(String salt, String storedHash) {

		try {
			String passwordHash = Utility.SHA256(password);
			String requestHash = Utility.SHA256(passwordHash + salt);
			return requestHash.equals(storedHash);
		} catch (NoSuchAlgorithmException e) {
			// TODO
			e.printStackTrace();
			return false;
		}
	}
}
