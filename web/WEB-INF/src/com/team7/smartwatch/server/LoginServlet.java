package com.team7.smartwatch.server;

import com.team7.smartwatch.shared.Utility;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = -4701061750449836620L;
	private static final String USERS_TABLE = "users";
    private static final String SELECT_STATEMENT =
    		"SELECT userPass, salt FROM " + USERS_TABLE +
    		" WHERE userID = ?";
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
        String requestPassword = null;
        String storedHash = null;
        String salt = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement selectUser = conn.prepareStatement(SELECT_STATEMENT);
            
            // As we can't pass strings by reference, we use temporary arrays
            // instead.
            String[] arrayID = {""};
            String[] arrayPassword = {""};
            getRequestCredentials(request, arrayID, arrayPassword);
            String requestUserID = arrayID[0];
            requestPassword = arrayPassword[0];

            bindValues(selectUser, requestUserID);
            ResultSet resultSet = selectUser.executeQuery();
            if (resultSet.next()) {
            	storedHash = resultSet.getString("userPass");
            	salt = resultSet.getString("salt");
            } else {
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
        	return false;
        }
        return passwordIsCorrect(requestPassword, storedHash, salt);
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

    private void bindValues(PreparedStatement selectUser, String userID)
            throws BadPostParameterException {
    	
        try {
            // TODO: validate userID
            selectUser.setString(1, userID);
        } catch (SQLException e) {
        	// TODO: log
            e.printStackTrace();
        } catch (JSONException e) {
        	throw new BadPostParameterException();
        }
    }
    
	private void getRequestCredentials(HttpServletRequest request,
			String[] userID, String[] password) throws BadPostParameterException {
    	
        	JSONObject jObj;
			try {
				jObj = JSONConverter.getJSON(request);
				userID[0] = String.valueOf(jObj.getInt("userID"));
				password[0] = jObj.getString("password");
			} catch (IOException | JSONException e) {
				e.printStackTrace();
				throw new BadPostParameterException();
			}
    }
}