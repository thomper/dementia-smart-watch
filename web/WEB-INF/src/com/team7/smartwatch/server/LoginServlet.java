package com.team7.smartwatch.server;

import com.team7.smartwatch.shared.Utility;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = -4701061750449836620L;
	private static final Logger logger = Logger.getLogger(LoginServlet.class
			.getName());
	private static final String USERS_TABLE = "users";
	private static final String SELECT_STATEMENT = "SELECT userPass, salt FROM "
			+ USERS_TABLE + " WHERE userName = ?";
	private static final String SUCCESS_MESSAGE = "Login successful";
	private static final String ERROR_MESSAGE = "Authentication failed";

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		boolean succeeded = authenticate(request);
		if (succeeded) {
			out.println(SUCCESS_MESSAGE);
		} else {
			out.println(ERROR_MESSAGE);
		}
	}

	private boolean authenticate(HttpServletRequest request) {
		// TODO: Refactor! Extract database and business logic code.
		Connection conn = null;
		Credentials credentials = new Credentials();
		String storedHash = null;
		String salt = null;

		// Read password hash from database associated with given username.
		try {
			conn = DatabaseConnector.getConnection();
			PreparedStatement selectUser = conn
					.prepareStatement(SELECT_STATEMENT);
			getRequestCredentials(request, credentials);
			bindValues(selectUser, credentials.username);
			ResultSet resultSet = selectUser.executeQuery();
			if (resultSet.next()) {
				storedHash = resultSet.getString("userPass");
				salt = resultSet.getString("salt");
			} else {
				logger.log(Level.INFO,
						"Failed login attempt from " + request.getRemoteAddr()
								+ " for nonexistent username "
								+ credentials.username + ".");
				return false;
			}
		} catch (SQLException e) {
			// TODO
			e.printStackTrace();
		} catch (BadPostParameterException e) {
			logger.log(Level.INFO, "Bad post parameter given in login " + 
					"attempt from " + request.getRemoteAddr() + ".");
		} finally {

			// Close database connection.
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				// TODO
				e.printStackTrace();
			}
		}

		if (Utility.arrayContainsNull(credentials.password, storedHash, salt)) {
			logger.log(Level.INFO, "Error accessing user info from database.");
			return false;
		}

		boolean correct = passwordIsCorrect(credentials.password, salt,
				storedHash);
		if (correct) {
			logger.log(Level.INFO,
					"Successful login from " + request.getRemoteAddr()
							+ " for user " + credentials.username + ".");
		} else {
			logger.log(Level.INFO,
					"Failed login attempt from " + request.getRemoteAddr()
							+ " for user " + credentials.username + ".");
		}

		return correct;
	}

	private boolean passwordIsCorrect(String requestPassword,
			String salt, String storedHash) {

		try {
			String passwordHash = Utility.SHA256(requestPassword);
			String requestHash = Utility.SHA256(passwordHash + salt);
			return requestHash.equals(storedHash);
		} catch (NoSuchAlgorithmException e) {
			// TODO
			e.printStackTrace();
			return false;
		}
	}

	/* Place given username in prepared statement. */
	private void bindValues(PreparedStatement selectUser, String username)
			throws BadPostParameterException {

		try {
			selectUser.setString(1, username);
		} catch (SQLException e) {
			// TODO
			e.printStackTrace();
		} catch (JSONException e) {
			throw new BadPostParameterException();
		}
	}

	/* Get the username and password from the HTTP request. */
	private void getRequestCredentials(HttpServletRequest request,
			Credentials credentials) throws BadPostParameterException {

		JSONObject jObj;
		try {
			jObj = JSONConverter.getJSON(request);
			credentials.username = jObj.getString("username");
			credentials.password = jObj.getString("password");
		} catch (IOException | JSONException e) {
			throw new BadPostParameterException();
		}
	}
}