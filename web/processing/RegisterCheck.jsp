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
			String email = request.getParameter("email");
			String username = request.getParameter("username");
			String mobile = request.getParameter("mobile");
			String conNum = request.getParameter("contactnum");
			String fName = request.getParameter("fname");
			String lName = request.getParameter("lname");
			String pwdHash = null;
			String cPwdHash = null;		
			String storedEmail = null;
			String storedUsername = null;
			int assignedCarerId = 0;
			boolean valid = true;
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			java.sql.Connection conn;
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
			Statement st = conn.createStatement();
		
			String emailReg = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";
			String nameReg = "^[a-zA-Z][-'a-zA-Z]+$";
			String numberReg = "^(\\+|\\d)[0-9]{7,16}$";
			String usernamePasswordReg = "^[a-zA-Z0-9]+$";
			
			if (!fName.matches(nameReg)) {
				valid = false;
			} else if (!lName.matches(nameReg)) {
				valid = false;
			} else if (!mobile.matches(numberReg)) {
				valid = false;
			} else if (!conNum.matches(numberReg)) {
				valid = false;
			} else if (!email.matches(emailReg)) {
				valid = false;
			} else if (!username.matches(usernamePasswordReg)) {
				valid = false;
			} else if (!pwd.matches(usernamePasswordReg)) {
				valid = false;
			}

			if (!valid) {
				response.sendRedirect("../Error.jsp?error=9");
			} else {
				String query = "SELECT carerID FROM users WHERE email='"+email+
				"' OR userName='"+username+"'";

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
						query = "INSERT INTO carers (fName, lName, mobileNum, contactNum) VALUES ('"+fName+"', '"+
							lName+"', '"+mobile+"', '"+conNum+"')";
				
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
							email+"', '"+username+"', '"+sb.toString()+"', '"+salt+"')";
							
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
			}

			st.close();
			conn.close();

		 %>

    </body>
</html>