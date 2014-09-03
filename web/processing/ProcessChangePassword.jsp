g<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.security.MessageDigest" %>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
        <title>ProcessChangePassword</title>
		<%
			//If not logged in - redirect to error page and cancel processing od remaining jsp
			if (session.getAttribute("userid") == null) { response.sendRedirect("../Error.jsp?error=5"); return; }
		%>	
    </head>
    <body>
		<%		
			String userID = session.getAttribute("userid").toString();
			String oldPW = request.getParameter("currentPassword");
			String newPW = request.getParameter("password");
			String cNewPW = request.getParameter("confirmPassword");
			String oldPWHash = "";
			String storedHash = "";
			String salt = "";
		
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			java.sql.Connection conn;
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
			Statement st = conn.createStatement();
			
			ResultSet rs = st.executeQuery("SELECT userPass, salt FROM users WHERE userID='"+userID+"';");

			while (rs.next()) { 
				storedHash = rs.getString(1);
				salt = rs.getString(2);
			}
				
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(oldPW.getBytes());	
			byte byteData[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			oldPWHash = sb.toString();
			
			md.update((oldPWHash+salt).getBytes());	
			byteData = md.digest();
			sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			oldPWHash = sb.toString();
			
			if (!oldPWHash.equals(storedHash)) {
				//Wrong PW
				response.sendRedirect("../Error.jsp?error=7");
			}
			else if (!newPW.equals(cNewPW)) {
				//PWs dont match
				response.sendRedirect("../Error.jsp?error=4");			
			}
			else {
				String newPWHash = "";
			
				md.update(newPW.getBytes());	
				byteData = md.digest();
				sb = new StringBuffer();
				for (int i = 0; i < byteData.length; i++) {
					sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
				}
				newPWHash = sb.toString();

				md.update((newPWHash+salt).getBytes());	
				byteData = md.digest();
				sb = new StringBuffer();
				for (int i = 0; i < byteData.length; i++) {
					sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
				}
				newPWHash = sb.toString();	
				
				st.executeUpdate("UPDATE users SET userPass='"+newPWHash+"' WHERE userID='"+userID+"';");		
				
				response.sendRedirect("../ChangePassword.jsp?success=1");					
			}

			rs.close();
			st.close();
			conn.close();	
				
		%>
    </body>
</html>