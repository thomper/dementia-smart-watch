<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.security.MessageDigest" %>
<%@ page import="java.util.UUID" %>
<!DOCTYPE html>

<html>
    <head profile="http://www.w3.org/2005/10/profile">
		<title>Register – DementiaWatch Web Client</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
		<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">
		<link rel="stylesheet" type="text/css" href="css/mystyle.css">
		<script src="scripts/register.js" type="text/javascript"></script>
    </head>
	
    <body>
	<div id="container">
		
		<div id="header">
			<div id="header-left">
				
			</div>
			<div id="header-right">
				
			</div>
			<div id="header-middle">
				<p>DementiaWatch Web Client</p>
			</div>			
		</div>
	
		<div id="content">
			<h1>Create An Account</h1>
			<%
				String submitted = request.getParameter("submitted");
				
				if (submitted != null) {
					String pwd=request.getParameter("password");
					String cPwd=request.getParameter("confirmpassword");
					String email = request.getParameter("email");
					String username = request.getParameter("username");
					String mobile = request.getParameter("mobile");
					String conNum = request.getParameter("contactnum");
					String fName = request.getParameter("fname");
					String lName = request.getParameter("lname");
					String fNameMessage = "",
						   lNameMessage = "",
						   mobileMessage = "",
						   usernameMessage = "",
						   emailMessage = "",
						   alternateMessage = "",
						   passwordMessage = "",
						   confirmPasswordMessage = "";
					String pwdHash = null;	
					int assignedCarerId = 0;
					boolean valid = true;
					
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					java.sql.Connection conn;
					
					try {
						conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
						Statement st = conn.createStatement();
						java.sql.Connection conn2;
						conn2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
						Statement st2 = conn2.createStatement();
					
						String emailReg = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";
						String nameReg = "^[a-zA-Z][-'a-zA-Z]+$";
						String numberReg = "^(\\+|\\d)[0-9]{7,16}$";
						String usernamePasswordReg = "^[a-zA-Z0-9]+$";
			
						ResultSet rs = st.executeQuery("SELECT carerID FROM users WHERE userName='"+username+"'");
						ResultSet rs2 = st2.executeQuery("SELECT carerID FROM users WHERE email='"+email+"'");
	
						if (!fName.matches(nameReg)) {
							fNameMessage = "First name can only contain letters (can have a hyphen or apostrophe)";
							valid = false;
						}
						if (!lName.matches(nameReg)) {
							lNameMessage = "Last name can only contain letters (can have a hyphen or apostrophe)";
							valid = false;
						}
						if (!mobile.matches(numberReg)) {
							mobileMessage = "Invalid mobile number";
							valid = false;
						}
	
						if (!conNum.matches(numberReg)) {
							alternateMessage = "Invalid contact number";
							valid = false;
						}
	
						if (!email.matches(emailReg)) {
							emailMessage = "Invalid email address";
							valid = false;
						}
	
						if (!username.matches(usernamePasswordReg)) {
							usernameMessage = "Username must only include letters or numbers";
							valid = false;
						}
	
						if (!pwd.matches(usernamePasswordReg) || pwd.length() < 6) {
							passwordMessage = "Password must only include letters or numbers and be at least 6 chars long";
							valid = false;
						} 
	
						if (rs.next()) {
							usernameMessage = "Username already in use";
							valid = false;
						}
						
						if (rs2.next()) {
							emailMessage = "Email already in use";
							valid = false;
						} 
						
						if (pwd.equals(cPwd) == false) {
							confirmPasswordMessage = "Passwords do not match";
							valid = false;
						} 
			
						if (!valid) {
							out.println("<form class='pure-form pure-form-stacked' action='Register.jsp' method='post'>");
								out.println("<fieldset>");
									out.println("<legend><b>Your Details:</b></legend>");
												
									out.println("<label for='username'>First Name</label>");
									out.println("<center><input id='firstName' name='fname' type='text' placeholder='First Name' onblur='checkFirstName();' maxlength='20' value='"+fName+"' required>");
									out.println("<span id='firstNameMessage' style='color:#ff6666' class='firstNameMessage'>"+fNameMessage+"</span></center>");
									
									out.println("<label for='username'>Last Name</label>");
									out.println("<center><input id='lastName' name='lname' type='text' placeholder='Last Name' onblur='checkLastName();' maxlength='30' value='"+lName+"' required>");
									out.println("<span id='lastNameMessage' style='color:#ff6666' class='lastNameMessage'>"+lNameMessage+"</span></center>");
									
									out.println("<label for='username'>Mobile #</label>");
									out.println("<center><input id='mobile' name='mobile' type='number' onblur='checkMobile();' placeholder='0412345678' maxlength='10' value='"+mobile+"' required>");
									out.println("<span id='mobileMessage' style='color:#ff6666' class='mobileMessage'>"+mobileMessage+"</span></center>");
									
									out.println("<label for='username'>Alternate Contact #</label>");
									out.println("<center><input id='alternateMobile' name='contactnum' type='number' onblur='checkAlternateMobile();' placeholder='0712345678' maxlength='10' value='"+conNum+"' required>");
									out.println("<span id='alternateMobileMessage' style='color:#ff6666' class='alternateMobileMessage'>"+alternateMessage+"</span></center>");
									
									out.println("<legend><b>Account Details:</b></legend>");
									
									out.println("<label for='email'>Email Address</label>");
									out.println("<center><input id='email' name='email' type='email' placeholder='foo@bar.com' onblur='checkEmail();' maxlength='50' value='"+email+"' required>");
									out.println("<span id='emailMessage' style='color:#ff6666' class='emailMessage'>"+emailMessage+"</span></center>");
									
									out.println("<label for='username'>User Name</label>");
									out.println("<center><input id='username' name='username' type='text' placeholder='Prefered Username' onblur='checkUsername();' maxlength='15' value='"+username+"' required>");
									out.println("<span id='usernameMessage' style='color:#ff6666' class='usernameMessage'>"+usernameMessage+"</span></center>");
				
									out.println("<label for='password'>Password</label>");
									out.println("<center><input id='password' name='password' type='password' placeholder='Password' onblur='checkPassword();' maxlength='20' required>");
									out.println("<span id='passwordMessage' style='color:#ff6666' class='passwordMessage'>"+passwordMessage+"</span></center>");
									
									out.println("<label for='password'>Confirm Password</label>");
									out.println("<center><input id='confirmPassword' name='confirmpassword' type='password' placeholder='Confirm Password' onblur='checkConfirmPassword();' maxlength='20' required>");
									out.println("<span id='confirmPasswordMessage' style='color:#ff6666' class='confirmPasswordMessage'>"+confirmPasswordMessage+"</span><center>");
									
									out.println("<input name='submitted' type='hidden' value='submitted'>");
				
									out.println("<button type='submit' name='register' class='pure-button pure-button-primary' value='register'>Create Account!</button>");
								out.println("</fieldset>");
							out.println("</form>");	
						} else {
							String input = pwd;
								
							MessageDigest md = MessageDigest.getInstance("SHA-256");
							md.update(input.getBytes());
								
							byte byteData[] = md.digest();
							StringBuffer sb = new StringBuffer();
							for (int i = 0; i < byteData.length; i++) {
								sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
							}
							pwdHash = sb.toString();
							
							st.executeUpdate("INSERT INTO carers (fName, lName, mobileNum, contactNum) VALUES ('"+fName+"', '"+lName+"', '"+mobile+"', '"+conNum+"')");
	
							rs = st.executeQuery("SELECT LAST_INSERT_ID()");
							rs.next();
							assignedCarerId = rs.getInt(1);
							
							String salt = UUID.randomUUID().toString();
							
							md.update((pwdHash+salt).getBytes());				
							byteData = md.digest();
							sb = new StringBuffer();
							for (int i = 0; i < byteData.length; i++) {
								sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
							}
	
							st.executeUpdate("INSERT INTO users (carerID, email, userName, userPass, salt) VALUES ('"+assignedCarerId+"', '"+email+"', '"+username+"', '"+sb.toString()+"', '"+salt+"')");
							
							session.removeAttribute("username");
							session.removeAttribute("password");
							session.removeAttribute("userid");
							session.removeAttribute("carerid");
							session.invalidate();
							response.sendRedirect("RegisterSuccess.jsp");
							return;						
						}
						st.close();
						conn.close();
					} catch (Exception e) {
						response.sendRedirect("../Error.jsp?error=9");
					}
				} else {%>
					<form class="pure-form pure-form-stacked" action="Register.jsp" method="post">
						<fieldset>
							<legend><b>Your Details:</b></legend>
							
							<label for="username">First Name</label>
							<center><input id="firstName" name="fname" type="text" placeholder="First Name" onblur="checkFirstName();" maxlength='20' required>
							<span id="firstNameMessage" class="firstNameMessage"></span></center>
							
							<label for="username">Last Name</label>
							<center><input id="lastName" name="lname" type="text" placeholder="Last Name" onblur="checkLastName(); return false;" maxlength='30' required>
							<span id="lastNameMessage" class="lastNameMessage"></span></center>
							
							<label for="username">Mobile #</label>
							<center><input id="mobile" name="mobile" type="number" onblur="checkMobile(); return false;" placeholder="0412345678" maxlength='10' required>
							<span id="mobileMessage" class="mobileMessage"></span></center>
							
							<label for="username">Alternate Contact #</label>
							<center><input id="alternateMobile" name="contactnum" type="number" onblur="checkAlternateMobile(); return false;" placeholder="0712345678" maxlength='10' required>
							<span id="alternateMobileMessage" class="alternateMobileMessage"></span></center>
							
							<legend><b>Account Details:</b></legend>
							
							<label for="email">Email Address</label>
							<center><input id="email" name="email" type="email" placeholder="foo@bar.com" onblur="checkEmail(); return false;" maxlength='50' required>
							<span id="emailMessage" class="emailMessage"></span></center>
							
							<label for="username">User Name</label>
							<center><input id="username" name="username" type="text" placeholder="Preferred Username" onblur="checkUsername(); return false;" maxlength='15' required>
							<span id="usernameMessage" class="usernameMessage"></span></center>
		
							<label for="password">Password</label>
							<center><input id="password" name="password" type="password" placeholder="Password" onblur="checkPassword(); return false;" maxlength='20' required>
							<span id="passwordMessage" class="passwordMessage"></span></center>
							
							<label for="password">Confirm Password</label>
							<center><input id="confirmPassword" name="confirmpassword" type="password" placeholder="Confirm Password" onblur="checkConfirmPassword(); return false;" maxlength='20' required>
							<span id="confirmPasswordMessage" class="confirmPasswordMessage"></span><center>
							
							<input name="submitted" type="hidden" value="submitted">
		
							<button type="submit" name="register" class="pure-button pure-button-primary" value="register">Create Account!</button>
						</fieldset>
					</form>		
				<%}%>
			<br>
			<h4><a href="index.jsp">Back To Login<a></h4>
					
		</div>	
	
	<jsp:include page = "includes/footer.jsp" flush = "true" />