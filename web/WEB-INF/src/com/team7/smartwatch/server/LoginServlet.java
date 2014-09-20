package com.team7.smartwatch.server;

import com.team7.smartwatch.shared.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = -4701061750449836620L;
	private static final Logger logger = Logger.getLogger(LoginServlet.class
			.getName());
	private static final String SUCCESS_MESSAGE = "Login successful";
	private static final String ERROR_MESSAGE = "Authentication failed";

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// TODO: we should be using JSON instead of text for the response
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		boolean succeeded = authenticate(request);
		if (succeeded) {
			out.println(SUCCESS_MESSAGE);
		} else {
			out.println(ERROR_MESSAGE);
		}
	}

	/* Returns true if the request contains a matching username and password,
	 * false otherwise.
	 */
	private boolean authenticate(HttpServletRequest request) {

		Credentials credentials = getRequestCredentials(request);
		if (credentials == null || credentials.username == null ||
				credentials.password == null) {
			return false;
		}

		User user = getUser(credentials);
		boolean usernameExists = user != null;
		boolean succeeded = usernameExists &&
				credentials.passwordIsCorrect(user.salt, user.storedHash);
		if (succeeded) {
			HttpSession session = request.getSession();
			session.setAttribute("userID", user.userID);
		}
		logLoginAttempt(succeeded, usernameExists, credentials.username,
				request.getRemoteAddr());
		return succeeded;
	}

	/**
	 * Gets the username and password from the HTTP request.
	 * 
	 * @param  request the request to read from.
	 * @return the credentials read from the request or null if they could not
	 * 		   be read.
	 */
	private Credentials getRequestCredentials(HttpServletRequest request) {

		Credentials credentials = null;
		
		try {
			JSONObject jObj = JSONConverter.getJSON(request);
			credentials = new Credentials();
			credentials.username = jObj.getString("username");
			credentials.password = jObj.getString("password");
		} catch (IOException | JSONException e) {
			logger.log(Level.INFO, "Bad post parameter given in login " + 
					"attempt from " + request.getRemoteAddr() + ".");
		}
		
		return credentials;
	}
	
	/**
	 * Gets the user details associated with the username in credentials.
	 * 
	 * @param  credentials the object containing the username.
	 * @param  remoteAddress the address the request came from.
	 * @return the user with filled out details or null if the user could not be
	 * 		   read.
	 */
	private User getUser(Credentials credentials) {

		User user = null;

		try {
			user = DatabaseUserReader
					.readUserByUsername(credentials.username);
		} catch (BadSQLParameterException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage());
		}
		
		return user;
	}
	
	private void logLoginAttempt(boolean succeeded, boolean usernameExists,
			String username, String remoteAddress) {

		if (!usernameExists) {
            logger.log(Level.INFO, "Failed login attempt from " +
                    remoteAddress + " for nonexistent username " +
                    username + ".");
		} else if (succeeded) {
			logger.log(Level.INFO,
					"Successful login from " + remoteAddress + " for user " +
							username + ".");
		} else {
			logger.log(Level.INFO,
					"Failed login attempt from " + remoteAddress +
							" for user " + username + ".");
		}
	}
}