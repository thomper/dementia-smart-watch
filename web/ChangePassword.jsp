<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html>

<html>
    <head profile="http://www.w3.org/2005/10/profile">
		<title>Change Password – DementiaWatch Web Client</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
		<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">
		<link rel="stylesheet" type="text/css" href="css/mystyle.css">
		<link rel="stylesheet" type="text/css" href="css/message.css">

		<%
			//Has password just been successfully changed?
			//Displays the success banner
			int success = 0;
			try { success = Integer.parseInt(request.getParameter("success")); }
			catch (Exception e) { success = 0; }		
			
			if (success == 1) {
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
			}
		%>
			
		<%
			//If not logged in - redirect to error page and cancel processing od remaining jsp
			if (session.getAttribute("userid") == null) { response.sendRedirect("Error.jsp?error=5"); return; }
		%>
    </head>
	
    <body>
	<div id="container">
		
		<div id="header">
			<div id="header-left">
				<p>Location: <a href="Home.jsp">Home</a> > Change Password</p>
			</div>
			<div id="header-right">
				<p><a href="ChangePassword.jsp">Change PW</a> | <a href="Logout.jsp">Logout</a></p>				
			</div>
			<div id="header-middle">
				<p>DementiaWatch Web Client</p>
			</div>			
		</div>
	
		<div id="content">
		
			<!-- Success banner. Visibility toggled with jquery-->
			<div id='message' style="display: none;">
				<span>Your password has been changed successfully.</span>
				<a href="#" class="close-notify">X</a>
			</div>		
			
			<h1>Change Password</h1>
			
			<form class='pure-form pure-form-aligned' action='processing/ProcessChangePassword.jsp' method='post'>
				<fieldset>
					
					<div class='pure-control-group'>
						<label for='currentPassword'>Current Password</label>
						<input name='currentPassword' type='password' maxlength='64' required>
					</div>

					<div class='pure-control-group'>
						<label for='password'>New Password</label>
						<input name='password' type='password' maxlength='64' required>
					</div>
					
					<div class='pure-control-group'>
						<label for='confirmPassword'>Confirm New Password</label>
						<input name='confirmPassword' type='password' maxlength='64' required>
					</div>
					
					<div class='pure-controls'>
						<button type='submit' class='pure-button pure-button-primary'>Submit</button>
					</div>		
				</fieldset>
			</form>
			
		</div>	
		
	</div>
    </body>
</html>