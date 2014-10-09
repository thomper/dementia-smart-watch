<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.security.MessageDigest" %>
<!DOCTYPE html>

<html>
    <head profile="http://www.w3.org/2005/10/profile">
		<title>Change Password – DementiaWatch Web Client</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
		<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">
		<link rel="stylesheet" type="text/css" href="css/mystyle.css">
		<link rel="stylesheet" type="text/css" href="css/message.css">
		<script src="scripts/register.js" type="text/javascript"></script>
		<%
			//If not logged in - redirect to error page and cancel processing od remaining jsp
			if (session.getAttribute("userid") == null) { response.sendRedirect("Error.jsp?error=5"); return; }
		%>
    <%-- This will add the rest of the head tag and navigation and alerts --%>	
		<jsp:include page = "includes/header.jsp" flush = "true" />
		<jsp:include page = "includes/headerC.jsp" flush = "true" />
	
		<div id="content">
			<!-- Success banner. Visibility toggled with jquery-->
			<div id='message' style="display: none;">
				<span>Your password has been changed successfully.</span>
				<a href="#" class="close-notify">X</a>
			</div>		
			
			<h1>Change Password</h1>
			
			<%
				String submitted = request.getParameter("submitted");
				String oldPasswordMessage = "";
				String passwordMessage = "";
				String confirmPasswordMessage = "";
				
				if (submitted != null) {
					boolean valid = true;
					String userID = session.getAttribute("userid").toString();
					String oldPW = request.getParameter("currentPassword");
					String newPW = request.getParameter("password");
					String cNewPW = request.getParameter("confirmPassword");
					String oldPWHash = "";
					String storedHash = "";
					String salt = "";
					
					MessageDigest md = MessageDigest.getInstance("SHA-256");
					StringBuffer sb = new StringBuffer();
					
					Class.forName("com.mysql.jdbc.Driver").newInstance();
					java.sql.Connection conn;
					conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
					Statement st = conn.createStatement();			
					ResultSet rs = st.executeQuery("SELECT userPass, salt FROM users WHERE userID='"+userID+"';");
			
					if (rs.next()) { 
						storedHash = rs.getString(1);
						salt = rs.getString(2);
						
						md.update(oldPW.getBytes());	
						byte byteData[] = md.digest();
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
							oldPasswordMessage = "You entered your current password incorrectly";
							valid = false;
						}
						
						if (!newPW.equals(cNewPW)) {
							passwordMessage = "Your new password does not match";
							valid = false;
						}
						
						String usernamePasswordReg = "^[a-zA-Z0-9]+$";
						
						if (!cNewPW.matches(usernamePasswordReg) || cNewPW.length() < 6) {
							valid = false;
						}
					}				
					
					if (!valid) {
						submitted = null;
					} else {
						String newPWHash = "";
						
						md.update(newPW.getBytes());	
						byte byteData[] = md.digest();
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
						
						rs.close();
						st.close();
						conn.close();
					
						out.println("<script src='scripts/jquery-2.1.1.min.js'></script>");
						out.println("<script type='text/javascript'>");
							out.println("$(document).ready(function() {");
								out.println("$('#message').fadeIn('slow');");
								out.println("$('#message a.close-notify').click(function() {");
									out.println("$('#message').fadeOut('slow');");
									out.println("return false;");
								out.println("});");
							out.println("});");
						out.println("</script>");
						
						oldPasswordMessage = "";
						passwordMessage = "";
						confirmPasswordMessage = "";
						
						submitted = null;
					}
				}
				
				if (submitted == null) {
			%>
			
			<form class='pure-form pure-form-aligned' action='ChangePassword.jsp' method='post'>
				<fieldset>
					
					<div class='pure-control-group'>
						<label for='currentPassword'>Current Password</label>
						<input id='oldPassword' name='currentPassword' type='password' maxlength='64' required>
						<span id='oldPasswordMessage' style='color:#ff6666' class='passwordMessage'><%=oldPasswordMessage%></span>
					</div>

					<div class='pure-control-group'>
						<label for='password'>New Password</label>
						<input id='password' name='password' type='password' onblur='checkPassword(); return false;' maxlength='64' required>
						<span id='passwordMessage' style='color:#ff6666' class='passwordMessage'><%=passwordMessage%></span>
					</div>
					
					<div class='pure-control-group'>
						<label for='confirmPassword'>Confirm New Password</label>
						<input id='confirmPassword' name='confirmPassword' type='password' onblur='checkConfirmPassword(); return false;' maxlength='64' required>
						<span id='confirmPasswordMessage' style='color:#ff6666' class='confirmPasswordMessage'><%=confirmPasswordMessage%></span>
					</div>
					
					<input name='submitted' type='hidden' value='submitted'>
					
					<div class='pure-controls'>
						<button type='submit' class='pure-button pure-button-primary'>Submit</button>
					</div>		
				</fieldset>
			</form>
			<%
				}
			%>
			
		</div>	
		
	<jsp:include page = "includes/footer.jsp" flush = "true" />
	