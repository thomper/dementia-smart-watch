<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html>

<html>
    <head profile="http://www.w3.org/2005/10/profile">
		<title>My Patient – DementiaWatch Web Client</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
		<link rel="stylesheet" type="text/css" href="css/mystyle.css">
		<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">	
		<link rel="stylesheet" type="text/css" href="css/message.css">
		<script src="scripts/maps.js" type="text/javascript"></script>

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
    
	<%-- This will add the rest of the head tag and navigation and alerts --%>	
	<jsp:include page = "includes/header.jsp" flush = "true" />
	<jsp:include page = "includes/headerP.jsp" flush = "true" />
				
		<div id="header2">
			<div id="header-left">
				<p>Location: <a href="Home.jsp">Home</a> > My Patient</p>
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
				<span>The patient's details have been successfully updated.</span>
				<a href="#" class="close-notify">X</a>
			</div>
			
			<h1>Add / Update Patient Details</h1>
			
			<%
				String carerID = session.getAttribute("carerid").toString();
			
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				java.sql.Connection conn;
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=root");
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery("SELECT * FROM patients WHERE carerID='"+carerID+"'");		
				
				if(rs.next()) {
					
					out.println("<form class='pure-form pure-form-aligned' action='processing/UpdatePatient.jsp?patientexists=true' method='post'>");
						out.println("<fieldset>");
						
							out.println("<div class='pure-control-group'>");
								out.println("<label for='patientID'>Patient ID</label>");
								out.println("<input name='patientID' type='text' value='"+rs.getString(1)+"' disabled>");
							out.println("</div>");
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='firstName'>First Name</label>");
								out.println("<input name='firstName' type='text' value='"+rs.getString(3)+"' required>");
							out.println("</div>");
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='surname'>Surname</label>");
								out.println("<input name='surname' type='text' value='"+rs.getString(4)+"' required>");
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
								out.println("<input name='age' type='number' value='"+rs.getString(6)+"' required>");
							out.println("</div>");							
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='bloodType'>Blood Type</label>");
								out.println("<input name='bloodType' type='text' value='"+rs.getString(7)+"'>");
							out.println("</div>");		

							out.println("<div class='pure-control-group'>");
								out.println("<label for='medication'>Medication</label>");
								out.println("<input name='medication' type='text' value='"+rs.getString(8)+"'>");
							out.println("</div>");		

							out.println("<div class='pure-control-group'>");
								out.println("<label for='status'>Status</label>");
								out.println("<input name='status' type='text' value='"+rs.getString(9)+"'>");
							out.println("</div>");		
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='address'>Home Address</label>");
								out.println("<input name='address' type='text' value='"+rs.getString(10)+"' required>");
							out.println("</div>");		
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='suburb'>Home Suburb</label>");
								out.println("<input name='suburb' type='text' value='"+rs.getString(11)+"' required>");
							out.println("</div>");									
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='conNum'>Contact Number</label>");
								out.println("<input name='conNum' type='text' value='"+rs.getString(12)+"' required>");
							out.println("</div>");		

							out.println("<div class='pure-control-group'>");
								out.println("<label for='emergName'>Emergency Contact Name</label>");
								out.println("<input name='emergName' type='text' value='"+rs.getString(13)+"'>");
							out.println("</div>");		
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='emergAddress'>Emergency Contact Address</label>");
								out.println("<input name='emergAddress' type='text' value='"+rs.getString(14)+"'>");
							out.println("</div>");		

							out.println("<div class='pure-control-group'>");
								out.println("<label for='emergSuburb'>Emergency Contact Suburb</label>");
								out.println("<input name='emergSuburb' type='text' value='"+rs.getString(15)+"'>");
							out.println("</div>");		

							out.println("<div class='pure-control-group'>");
								out.println("<label for='emergNum'>Emergency Contact Number</label>");
								out.println("<input name='emergNum' type='text' value='"+rs.getString(16)+"' required>");
							out.println("</div>");	

							out.println("<div class='pure-control-group'>");
								out.println("<label for='uniqueKey'>Unique Key</label>");
								out.println("<input name='uniqueKey' type='text' value='"+rs.getString(17)+"' required>");
							out.println("</div>");

							out.println("<div class='pure-controls'>");
								out.println("<button type='submit' class='pure-button pure-button-primary'>Submit</button>");
							out.println("</div>");
							
						out.println("</fieldset>");
					out.println("</form>");			
					
					Statement st2 = conn.createStatement();
					ResultSet rs2 = st2.executeQuery("SELECT fName, lName, status, patientLat, patientLong FROM patients JOIN patientLoc on patientLoc.patientID = patients.patientID WHERE patientLoc.patientID=" + rs.getString(1));					

					if (rs2.next()) {
						
						Double lat = rs2.getDouble(4);
						Double longtitude = rs2.getDouble(5);
						String status = rs2.getString(3);
						String name = rs2.getString(1) + " " + rs2.getString(2);
						
						%>						
						<div id="mapcanvas" style="height:300px; width:500px">
							<script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false"></script>
							<script>google.maps.event.addDomListener(window, 'load', initialize(<%=lat%>, <%=longtitude%>, '<%=name%>', '<%=status%>'));</script>
						</div>
						<%
}						
			
				}				
				else {
					//no associated patient yet
									
					out.println("<form class='pure-form pure-form-aligned' action='processing/UpdatePatient.jsp?patientexists=false' method='post'>");
						out.println("<fieldset>");
						
							out.println("<div class='pure-control-group'>");
								out.println("<label for='patientID'>Patient ID</label>");
								out.println("<input name='patientID' type='text' placeholder='Not Yet Assigned' disabled>");
							out.println("</div>");
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='firstName'>First Name</label>");
								out.println("<input name='firstName' type='text' placeholder='First Name' required>");
							out.println("</div>");
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='surname'>Surname</label>");
								out.println("<input name='surname' type='text' placeholder='Surname' required>");
							out.println("</div>");
							
							out.println("<div class='pure-control-group'>");							
								out.println("<label for='gender'>Gender</label>");
								out.println("<select name='gender'>");
									out.println("<option>Male</option>");
									out.println("<option>Female</option>");
								out.println("</select>");		
							out.println("</div>");
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='age'>Age</label>");
								out.println("<input name='age' type='number' placeholder='00' required>");
							out.println("</div>");							
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='bloodType'>Blood Type</label>");
								out.println("<input name='bloodType' type='text' placeholder='O-'>");
							out.println("</div>");		

							out.println("<div class='pure-control-group'>");
								out.println("<label for='medication'>Medication</label>");
								out.println("<input name='medication' type='text' placeholder='Medication'>");
							out.println("</div>");		

							out.println("<div class='pure-control-group'>");
								out.println("<label for='status'>Status</label>");
								out.println("<input name='status' type='text' placeholder='Status'>");
							out.println("</div>");		
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='address'>Home Address</label>");
								out.println("<input name='address' type='text' placeholder='Home Address' required>");
							out.println("</div>");		
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='suburb'>Home Suburb</label>");
								out.println("<input name='suburb' type='text' placeholder='Home Suburb' required>");
							out.println("</div>");									
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='conNum'>Contact Number</label>");
								out.println("<input name='conNum' type='text' placeholder='Contact Number' required>");
							out.println("</div>");		

							out.println("<div class='pure-control-group'>");
								out.println("<label for='emergName'>Emergency Contact Name</label>");
								out.println("<input name='emergName' type='text' placeholder='Emergency Name'>");
							out.println("</div>");		
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='emergAddress'>Emergency Contact Address</label>");
								out.println("<input name='emergAddress' type='text' placeholder='Emergency Address'>");
							out.println("</div>");		

							out.println("<div class='pure-control-group'>");
								out.println("<label for='emergSuburb'>Emergency Contact Suburb</label>");
								out.println("<input name='emergSuburb' type='text' placeholder='Emergency Suburb'>");
							out.println("</div>");		

							out.println("<div class='pure-control-group'>");
								out.println("<label for='emergNum'>Emergency Contact Number</label>");
								out.println("<input name='emergNum' type='text' placeholder='Emergency Contact Number' required>");
							out.println("</div>");	

							out.println("<div class='pure-control-group'>");
								out.println("<label for='uniqueKey'>Unique Key</label>");
								out.println("<input name='uniqueKey' type='text' placeholder='Unique Key' required>");
							out.println("</div>");

							out.println("<div class='pure-controls'>");
								out.println("<button type='submit' class='pure-button pure-button-primary'>Submit</button>");
							out.println("</div>");
							
						out.println("</fieldset>");
					out.println("</form>");			
										
					
				}
				
				rs.close();
				st.close();
				conn.close();				
				
			%>			
			
		</div>	
		
	<jsp:include page = "includes/headerC.jsp" flush = "true" />