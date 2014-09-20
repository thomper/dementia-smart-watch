<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head profile="http://www.w3.org/2005/10/profile">
		<title>Add New Patient – DementiaWatch Web Client</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
		<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">	
		<link rel="stylesheet" type="text/css" href="css/mystyle.css">
		<link rel="stylesheet" type="text/css" href="css/message.css">
		<script src="scripts/maps.js" type="text/javascript"></script>

		<%
			//Has a patient just been successfully changed?
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
			
		<div id="content">
		
			<!-- Success banner. Visibility toggled with jquery-->
			<div id='message' style="display: none;">
				<span>The patient has been successfully created.</span>
				<a href="#" class="close-notify">X</a>
			</div>
			
			<h1>Create New Patient</h1>
			
			<%			

				out.println("<form class='pure-form pure-form-aligned' action='processing/UpdatePatientDetails.jsp?patientid=0' method='post'>");
					out.println("<fieldset>");
						out.println("<div class='left'>");
							out.println("<div class='pure-control-group'>");
								out.println("<label for='patID'>Patient ID</label>");
								out.println("<input name='patID' type='text' placeholder='Not Yet Assigned' disabled>");
							out.println("</div>");
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='firstName'>First Name</label>");
								out.println("<input name='firstName' type='text' placeholder='First Name' maxlength='20' required>");
							out.println("</div>");
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='surname'>Surname</label>");
								out.println("<input name='surname' type='text' placeholder='Surname' maxlength='30' required>");
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
								out.println("<input name='age' type='number' placeholder='01' min='1' max='125' required>");
							out.println("</div>");							
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='bloodType'>Blood Type</label>");
								out.println("<input name='bloodType' type='text' placeholder='O-' maxlength='3'>");
							out.println("</div>");		
	
							out.println("<div class='pure-control-group'>");
								out.println("<label for='medication'>Medication</label>");
								out.println("<input name='medication' type='text' placeholder='Medication' maxlength='255'>");
							out.println("</div>");		
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='address'>Home Address</label>");
								out.println("<input name='address' type='text' placeholder='Home Address' maxlength='100' required>");
							out.println("</div>");
						out.println("</div> <!-- left -->");
						
						
						out.println("<div class= 'left'>");		
							out.println("<div class='pure-control-group'>");
								out.println("<label for='suburb'>Home Suburb</label>");
								out.println("<input name='suburb' type='text' placeholder='Home Suburb' maxlength='20' required>");
							out.println("</div>");									
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='conNum'>Contact Number</label>");
								out.println("<input name='conNum' type='number' placeholder='Contact Number' min='0' max='9999999999' required>");
							out.println("</div>");		
	
							out.println("<div class='pure-control-group'>");
								out.println("<label for='emergName'>Emergency Contact Name</label>");
								out.println("<input name='emergName' type='text' placeholder='Emergency Name' maxlength='50'>");
							out.println("</div>");		
							
							out.println("<div class='pure-control-group'>");
								out.println("<label for='emergAddress'>Emergency Contact Address</label>");
								out.println("<input name='emergAddress' type='text' placeholder='Emergency Address' maxlength='100'>");
							out.println("</div>");		
	
							out.println("<div class='pure-control-group'>");
								out.println("<label for='emergSuburb'>Emergency Contact Suburb</label>");
								out.println("<input name='emergSuburb' type='text' placeholder='Emergency Suburb' maxlength='20'>");
							out.println("</div>");		
	
							out.println("<div class='pure-control-group'>");
								out.println("<label for='emergNum'>Emergency Contact Number</label>");
								out.println("<input name='emergNum' type='number' placeholder='Emergency Contact Number' min='0' max='9999999999' required>");
							out.println("</div>");	
						out.println("</div> <!-- left -->");
						
						out.println("<div class='pure-controls'>");
							out.println("<button type='submit' class='pure-button pure-button-primary'>Submit</button>");
						out.println("</div>");
						
					out.println("</fieldset>");
				out.println("</form>");							
				
			%>			
			
		</div>	
		
	<jsp:include page = "includes/footer.jsp" flush = "true" />
