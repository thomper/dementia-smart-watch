<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.security.MessageDigest" %>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">		
        <title>LoginCheck</title>
    </head>
    <body>
		<%	
        String usr=request.getParameter("username");
        String pwd=request.getParameter("password");
		String storedHash = null;
		String enteredHash = null;

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		java.sql.Connection conn;
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
		Statement st = conn.createStatement();
		
		String query = "SELECT userPass, salt, userID, carerID FROM users WHERE userName = '"+usr+"'";
		ResultSet rs = st.executeQuery(query);
		
		if (rs.next()) {
			storedHash = rs.getString(1);
			
			String input = pwd;
				
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(input.getBytes());
				
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			String tempHash = sb.toString();

			md.update((tempHash+rs.getString(2)).getBytes());				
			byteData = md.digest();
			sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			enteredHash = sb.toString();		
			
			if(enteredHash.equals(storedHash)) {
				session.setAttribute("userid", rs.getString(3));
				session.setAttribute("carerid", rs.getString(4));
				session.setAttribute("username", usr);
				response.sendRedirect("../Home.jsp");
			}
			else {
				response.sendRedirect("../Error.jsp?error=2");
			}
			
		}
		else {
			response.sendRedirect("../Error.jsp?error=1");
		}

		rs.close();
		st.close();
		conn.close();	

		 %>

    </body>
</html>