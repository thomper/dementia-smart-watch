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
	private static final Logger logger =
			Logger.getLogger(LoginServlet.class.getName());
	private static final String USERS_TABLE = "users";
    private static final String SELECT_STATEMENT =
    		"SELECT userPass, salt FROM " + USERS_TABLE +
    		" WHERE userName = ?";
    private static final String SUCCESS_MESSAGE = "Login successful";
    private static final String ERROR_MESSAGE = "Authentication failed";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    		throws IOException, ServletException
    {
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
        Connection conn = null;
        String requestUsername = null;
        String requestPassword = null;
        String storedHash = null;
        String salt = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement selectUser = conn.prepareStatement(SELECT_STATEMENT);
            
            // As we can't pass strings by reference, we use temporary arrays
            // instead.
            String[] arrayUsername = {""};
            String[] arrayPassword = {""};
            getRequestCredentials(request, arrayUsername, arrayPassword);
            requestUsername = arrayUsername[0];
            requestPassword = arrayPassword[0];

            bindValues(selectUser, requestUsername);
            ResultSet resultSet = selectUser.executeQuery();
            if (resultSet.next()) {
            	storedHash = resultSet.getString("userPass");
            	salt = resultSet.getString("salt");
            } else {
				logger.log(Level.INFO, "Failed login attempt from "
						+ request.getRemoteAddr() + " for nonexistent username "
						+ requestUsername + ".");
            	return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (BadPostParameterException e) {
            // TODO: log error?
		} finally {
			
			// Close database connection.
            try {
                if (conn != null) {
                    conn.close();
                    conn = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        if(Utility.arrayContainsNull(requestPassword, storedHash, salt)) {
        	logger.log(Level.INFO, "Error accessing user info from database.");
        	return false;
        }
        
        boolean correct = passwordIsCorrect(requestPassword, storedHash, salt);
        if (correct) {
			logger.log(Level.INFO,
					"Successful login from " + request.getRemoteAddr()
							+ " for user " + requestUsername + ".");
        } else {
			logger.log(Level.INFO,
					"Failed login attempt from " + request.getRemoteAddr()
							+ " for user " + requestUsername + ".");
        }

        return correct;
    }
    
	private boolean passwordIsCorrect(String requestPassword,
			String storedHash, String salt) {

		String requestHash = null;
		
		try {
			String passwordHash = Utility.SHA256(requestPassword);
			requestHash = Utility.SHA256(passwordHash + salt);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return false;
		}
		
		return requestHash.equals(storedHash);
    }

    private void bindValues(PreparedStatement selectUser, String username)
            throws BadPostParameterException {
    	
        try {
            // TODO: validate username
            selectUser.setString(1, username);
        } catch (SQLException e) {
        	// TODO: log
            e.printStackTrace();
        } catch (JSONException e) {
        	throw new BadPostParameterException();
        }
    }
    
	private void getRequestCredentials(HttpServletRequest request,
			String[] username, String[] password) throws BadPostParameterException {
    	
        	JSONObject jObj;
			try {
				jObj = JSONConverter.getJSON(request);
				username[0] = jObj.getString("username");
				password[0] = jObj.getString("password");
			} catch (IOException | JSONException e) {
				e.printStackTrace();
				throw new BadPostParameterException();
			}
    }
}