<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html>

<html>
    <head profile="http://www.w3.org/2005/10/profile">
		<title>Account Details – DementiaWatch Web Client</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
		<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">
		<link rel="stylesheet" type="text/css" href="css/mystyle.css">
		<link rel="stylesheet" type="text/css" href="css/message.css">

		<%
			//Have account details just been successfully changed?
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
		
    <%-- This will add the rest of the head tag and navigation and alerts --%>	
	<jsp:include page = "includes/header.jsp" flush = "true" />
				
		
		<div id="header2">
			<div id="header-left">
				<p>Location: <a href="Home.jsp">Home</a> > Account Details</p>
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
				<span>Your account details have been successfully updated.</span>
				<a href="#" class="close-notify">X</a>
			</div>		
			
			<h1>Update Account Details</h1>
			
			<%
				String carerID = session.getAttribute("carerid").toString();
			
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				java.sql.Connection conn;
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery("SELECT c.*, u.email FROM carers c, users u WHERE c.carerID='"+carerID+"' AND  u.carerID='"+carerID+"';");			
				
				if(rs.next()) {
					
					out.println("<form class='pure-form pure-form-aligned' action='processing/UpdateAccountDetails.jsp' method='post'>");
						out.println("<fieldset>");
						
							out.println("<div class='pure-control-group'>");
								out.println("<label for='carerID'>Carer ID</label>");
								out.println("<input name='carerID' type='text' value='"+rs.getString(1)+"' disabled>");
							out.println("</div>");
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='firstName'>First Name</label>");
								out.println("<input name='firstName' type='text' value='"+rs.getString(2)+"' maxlength='20' required>");
							out.println("</div>");
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='lastName'>Last Name</label>");
								out.println("<input name='lastName' type='text' value='"+rs.getString(3)+"' maxlength='30' required>");
							out.println("</div>");		

							out.println("<div class='pure-control-group'>");
								out.println("<label for='mobile'>Mobile #</label>");
								out.println("<input name='mobile' type='number' value='"+rs.getString(4)+"' min='0' max='9999999999' required>");
							out.println("</div>");			

							out.println("<div class='pure-control-group'>");
								out.println("<label for='contactNum'>Alternate Contact #</label>");
								out.println("<input name='contactNum' type='number' value='"+rs.getString(5)+"' min='0' max='9999999999' required>");
							out.println("</div>");	

							out.println("<div class='pure-control-group'>");
								out.println("<label for='email'>Email Address</label>");
								out.println("<input name='email' type='email' value='"+rs.getString(6)+"' maxlength='50' required>");
							out.println("</div>");							

							out.println("<div class='pure-controls'>");
								out.println("<button type='submit' class='pure-button pure-button-primary'>Update</button>");
							out.println("</div>");
							
						out.println("</fieldset>");
					out.println("</form>");			
									
				}				
				
				rs.close();				
				st.close();
				conn.close();				
				
			%>			
			
		</div>	
		
	</div>
    </body>
</html>