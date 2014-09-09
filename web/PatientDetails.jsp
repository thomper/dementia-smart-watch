<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html>

<html>
    <head profile="http://www.w3.org/2005/10/profile">
		<title>Update Patient Details – DementiaWatch Web Client</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
		<link rel="stylesheet" type="text/css" href="css/mystyle.css">
		<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">	
		<link rel="stylesheet" type="text/css" href="css/message.css">

		<%
			int patientID = 0;
			int success = 0;
			try { 
				patientID = Integer.parseInt(request.getParameter("patientid"));
			}
			catch (Exception e) { patientID = 0; }		
			try { 
				success = Integer.parseInt(request.getParameter("success")); 
			}
			catch (Exception e) { success = 0; }				
			
			//Have patient details just been successfully changed?
			//Displays the success banner
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
		
		<jsp:include page = "includes/header.jsp" flush = "true" />	
	
		<div id="content">
		
			<!-- Success banner. Visibility toggled with jquery-->
			<div id='message' style="display: none;">
				<span>The patient's details have been successfully updated.</span>
				<a href="#" class="close-notify">X</a>
			</div>
			
			<h1>Update Patient Details</h1>
			
			<%
				String carerID = session.getAttribute("carerid").toString();
			
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				java.sql.Connection conn;
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM patients WHERE patientID='"+patientID+"'");	
				
				if (rs.next()) {
					
					out.println("<form class='pure-form pure-form-aligned' action='processing/UpdatePatientDetails.jsp?patientid="+patientID+"' method='post'>");
						out.println("<fieldset>");
							otu.println("<div class = 'left'>");
								out.println("<div class='pure-control-group'>");
									out.println("<label for='patID'>Patient ID</label>");
									out.println("<input name='patID' type='text' value='"+patientID+"' disabled>");
								out.println("</div>");
								
								out.println("<div class='pure-control-group'>");
									out.println("<label for='firstName'>First Name</label>");
									out.println("<input name='firstName' type='text' value='"+rs.getString(3)+"' maxlength='20' required>");
								out.println("</div>");
								
								out.println("<div class='pure-control-group'>");
									out.println("<label for='surname'>Surname</label>");
									out.println("<input name='surname' type='text' value='"+rs.getString(4)+"' maxlength='30' required>");
								out.println("</div>");
								
								out.println("<div class='pure-control-group'>");							
									out.println("<label for='gender'>Gender</label>");
									out.println("<select name='gender'>");
									if (rs.getString(5).equals("Male") == true) {
										out.println("<option selected='Selected' value='Male'>Male</option>");
										out.println("<option value='Female'>Female</option>");
									} else {
										out.println("<option value='Male'>Male</option>");
										out.println("<option selected='Selected' value='Female'>Female</option>");
									}
									out.println("</select>");		
								out.println("</div>");
								
								out.println("<div class='pure-control-group'>");
									out.println("<label for='age'>Age</label>");
									out.println("<input name='age' type='number' value='"+rs.getString(6)+"' min='1' max='125' required>");
								out.println("</div>");							
								
								out.println("<div class='pure-control-group'>");
									out.println("<label for='bloodType'>Blood Type</label>");
									out.println("<input name='bloodType' type='text' value='"+rs.getString(7)+"' maxlength='3'>");
								out.println("</div>");		
	
								out.println("<div class='pure-control-group'>");
									out.println("<label for='medication'>Medication</label>");
									out.println("<input name='medication' type='text' value='"+rs.getString(8)+"' maxlength='255'>");
								out.println("</div>");			
							out.println("</div>");
							
							out.println("<div class='left'>);
								out.println("<div class='pure-control-group'>");
									out.println("<label for='address'>Home Address</label>");
									out.println("<input name='address' type='text' value='"+rs.getString(10)+"' maxlength='100' required>");
								out.println("</div>");		
								
								out.println("<div class='pure-control-group'>");
									out.println("<label for='suburb'>Home Suburb</label>");
									out.println("<input name='suburb' type='text' value='"+rs.getString(11)+"' maxlength='20' required>");
								out.println("</div>");									
								
								out.println("<div class='pure-control-group'>");
									out.println("<label for='conNum'>Contact Number</label>");
									out.println("<input name='conNum' type='number' value='"+rs.getString(12)+"' maxlength='10' required>");
								out.println("</div>");		
	
								out.println("<div class='pure-control-group'>");
									out.println("<label for='emergName'>Emergency Contact Name</label>");
									out.println("<input name='emergName' type='text' value='"+rs.getString(13)+"' maxlength='50'>");
								out.println("</div>");		
								
								out.println("<div class='pure-control-group'>");
									out.println("<label for='emergAddress'>Emergency Contact Address</label>");
									out.println("<input name='emergAddress' type='text' value='"+rs.getString(14)+"' maxlength='100'>");
								out.println("</div>");		
	
								out.println("<div class='pure-control-group'>");
									out.println("<label for='emergSuburb'>Emergency Contact Suburb</label>");
									out.println("<input name='emergSuburb' type='text' value='"+rs.getString(15)+"' maxlength='20'>");
								out.println("</div>");		
	
								out.println("<div class='pure-control-group'>");
									out.println("<label for='emergNum'>Emergency Contact Number</label>");
									out.println("<input name='emergNum' type='number' value='"+rs.getString(16)+"' maxlength='10' required>");
								out.println("</div>");	
	
								out.println("<div class='pure-control-group-3-4'>");
									out.println("<label for='uniqueKey'><br><b>Unique Key:</b> "+rs.getString(17)+"</label>");
								out.println("</div>");
							out.println("</div");
							
							out.println("<div class='pure-controls'>");
								out.println("<button type='submit' class='pure-button pure-button-primary'>Submit</button>");
							out.println("</div>");
							
						out.println("</fieldset>");
					out.println("</form>");			
					
				}
				else {
					response.sendRedirect("Error.jsp?error=8");
				}
				
				rs.close();
				st.close();
				conn.close();	
				
			%>						

		</div>	
		
	</div>
    </body>
</html>
