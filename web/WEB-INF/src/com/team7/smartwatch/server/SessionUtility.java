package com.team7.smartwatch.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtility {

	public static Integer getUserID(HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		
		return (Integer)session.getAttribute("userID");
	}
}
