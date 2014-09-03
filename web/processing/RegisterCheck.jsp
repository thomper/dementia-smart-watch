<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="java.util.UUID" %>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">		
        <title>RegisterCheck</title>
    </head>
    <body>
		<%
			String pwd=request.getParameter("password");
			String cPwd=request.getParameter("confirmpassword");
			String pwdHash = null;
			String cPwdHash = null;		
			String storedEmail = null;
			String storedUsername = null;
			int assignedCarerId = 0;
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			java.sql.Connection conn;
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
			Statement st = conn.createStatement();
			
			String query = "SELECT carerID FROM users WHERE email='"+request.getParameter("email")+
				"' OR userName='"+request.getParameter("username")+"'";

			ResultSet rs = st.executeQuery(query);
			
			if (!rs.next()) {
				String input = pwd;
				String input2 = cPwd;
					
				MessageDigest md = MessageDigest.getInstance("SHA-256");
				md.update(input.getBytes());
					
				byte byteData[] = md.digest();
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < byteData.length; i++) {
					sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
				}
				pwdHash = sb.toString();

				md.update(input2.getBytes());
				
				byteData = md.digest();
				sb = new StringBuffer();
				for (int i = 0; i < byteData.length; i++) {
					sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
				}
				cPwdHash = sb.toString();
				
				if (pwdHash.equals(cPwdHash)) {
					query = "INSERT INTO carers (fName, lName, mobileNum, contactNum) VALUES ('"+request.getParameter("fname")+"', '"+
						request.getParameter("lname")+"', '"+request.getParameter("mobile")+"', '"+request.getParameter("contactnum")+"')";
			
					st.executeUpdate(query);

					query = "SELECT LAST_INSERT_ID()";
					rs = st.executeQuery(query);
					rs.next();
					assignedCarerId = rs.getInt(1);
					
					String salt = UUID.randomUUID().toString();
					
					md.update((pwdHash+salt).getBytes());				
					byteData = md.digest();
					sb = new StringBuffer();
					for (int i = 0; i < byteData.length; i++) {
						sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
					}
					
					query = "INSERT INTO users (carerID, email, userName, userPass, salt) VALUES ('"+assignedCarerId+"', '"+
						request.getParameter("email")+"', '"+request.getParameter("username")+"', '"+sb.toString()+"', '"+salt+"')";
						
					st.executeUpdate(query);
					
					session.removeAttribute("username");
					session.removeAttribute("password");
					session.removeAttribute("userid");
					session.removeAttribute("carerid");
					session.invalidate();
					response.sendRedirect("../RegisterSuccess.jsp");
				}
				else {
					response.sendRedirect("../Error.jsp?error=4");
				}				
				
			}
			else {
				response.sendRedirect("../Error.jsp?error=3");
			}

			rs.close();
			st.close();
			conn.close();
		
		

		 %>

    </body>
</html>