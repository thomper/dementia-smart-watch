<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
        <title>UpdateAccountDetails</title>
		<%
			//If not logged in - redirect to error page and cancel processing do remaining jsp
			if (session.getAttribute("userid") == null) { response.sendRedirect("../Error.jsp?error=5"); return; }
		%>	
    </head>
    <body>
		<%		
			String carerID = session.getAttribute("carerid").toString();
			String userID = session.getAttribute("userid").toString();
		
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			java.sql.Connection conn;
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
			Statement st = conn.createStatement();
			
			ResultSet rs = st.executeQuery("SELECT COUNT(*), userID FROM users WHERE email='"+request.getParameter("email")+"';");
			int emailCount = 100;
			String checkID = "";
			while (rs.next()) { 
				emailCount = Integer.parseInt(rs.getString(1));
				checkID = rs.getString(2);
			}
			
			if (emailCount >= 1 && !checkID.equals(userID) ) {
				//Someone else is using that email
				response.sendRedirect("../Error.jsp?error=6");
			}
			else {
				st.executeUpdate("UPDATE carers SET fName='"+request.getParameter("firstName")+"', lName='"+request.getParameter("lastName")+
					"', mobileNum='"+request.getParameter("mobile")+"', contactNum='"+request.getParameter("contactNum")+"' WHERE carerID='"+carerID+"';");
					
				st.executeUpdate("UPDATE users SET email='"+request.getParameter("email")+"' WHERE userID='"+userID+"';");				
				
				response.sendRedirect("../AccountDetails.jsp?success=1");			
			}
				
			rs.close();
			st.close();
			conn.close();	
				
		%>
    </body>
</html>