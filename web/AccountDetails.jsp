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
				<span>Your account details have been successfully updated.</span>
				<a href="#" class="close-notify">X</a>
			</div>		
			
			<h1>Update Account Details</h1>
			
			<%
				String carerID = session.getAttribute("carerid").toString();
				String submitted = request.getParameter("submitted");
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			
				if (submitted != null) {
					String email = request.getParameter("email");
					String mobile = request.getParameter("mobile");
					String conNum = request.getParameter("contactNum");
					String fName = request.getParameter("firstName");
					String lName = request.getParameter("lastName");
					String fNameMessage = "",
						   lNameMessage = "",
						   mobileMessage = "",
						   emailMessage = "",
						   alternateMessage = "";
					boolean valid = true;
					
					String emailReg = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";
					String nameReg = "^[a-zA-Z][-'''a-zA-Z]+$";
					String numberReg = "^(\\+|\\d)[0-9]{7,16}$";
					
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
						   
					if (!valid) {
						out.println("<form class='pure-form pure-form-aligned' action='AccountDetails.jsp' method='post'>");
							out.println("<fieldset>");
									out.println("<input name='carerID' type='hidden' value='"+carerID+"' disabled>");
	
								out.println("<div class='pure-control-group'>");
									out.println("<label for='firstName'>First Name</label>");
									out.println("<input id='firstName' name='firstName' type='text' onblur='checkFirstName();' value='"+fName+"' maxlength='20' required>");
									out.println("<span id='firstNameMessage' style='color:#ff6666;' class='firstNameMessage'>"+fNameMessage+"</span>");
								out.println("</div>");
								
								out.println("<div class='pure-control-group'>");
									out.println("<label for='lastName'>Last Name</label>");
									out.println("<input id='lastName' name='lastName' type='text' onblur='checkLastName();' value='"+lName+"' maxlength='30' required>");
									out.println("<span id='lastNameMessage' style='color:#ff6666' class='lastNameMessage'>"+lNameMessage+"</span>");
								out.println("</div>");		
	
								out.println("<div class='pure-control-group'>");
									out.println("<label for='mobile'>Mobile #</label>");
									out.println("<input id='mobile' name='mobile' type='text' onblur='checkMobile();' value='"+mobile+"' required>");
									out.println("<span id='mobileMessage' style='color:#ff6666' class='mobileMessage'>"+mobileMessage+"</span>");
								out.println("</div>");			
	
								out.println("<div class='pure-control-group'>");
									out.println("<label for='contactNum'>Alternate Contact #</label>");
									out.println("<input id='alternateMobile' name='contactNum' type='text' onblur='checkAlternateMobile();' value='"+conNum+"' required>");
									out.println("<span id='alternateMobileMessage' style='color:#ff6666' class='alternateMobileMessage'>"+alternateMessage+"</span>");
								out.println("</div>");	
	
								out.println("<div class='pure-control-group'>");
									out.println("<label for='email'>Email Address</label>");
									out.println("<input id='email' name='email' type='email' onblur='checkEmail();' value='"+email+"' maxlength='50' required>");
									out.println("<span id='emailMessage' style='color:#ff6666' class='emailMessage'>"+emailMessage+"</span>");
								out.println("</div>");							
	
								out.println("<input name='submitted' type='hidden' value='submitted'>");
								
								out.println("<div class='pure-controls'>");
									out.println("<button type='submit' class='pure-button pure-button-primary'>Update</button>");
								out.println("</div>");
								
							out.println("</fieldset>");
						out.println("</form>");
					} else {
						String userID = session.getAttribute("userid").toString();
					
						java.sql.Connection conn;
						
						try {
							conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
							Statement st = conn.createStatement();
							
							ResultSet rs = st.executeQuery("SELECT COUNT(*), userID FROM users WHERE email='"+email+"';");
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
								st.executeUpdate("UPDATE carers SET fName='"+fName+"', lName='"+lName+"', mobileNum='"+mobile+"', contactNum='"+conNum+"' WHERE carerID='"+carerID+"';");
									
								st.executeUpdate("UPDATE users SET email='"+email+"' WHERE userID='"+userID+"';");
	
								submitted = null;
								
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
								
							rs.close();
							st.close();
							conn.close();
						} catch (Exception e) {
							response.sendRedirect("Error.jsp?error=9");
						}
					}
				} 
				
				if (submitted == null) {
					java.sql.Connection conn2;
					
					try {
						conn2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
						Statement st2 = conn2.createStatement();
						ResultSet rs2 = st2.executeQuery("SELECT c.*, u.email FROM carers c, users u WHERE c.carerID='"+carerID+"' AND  u.carerID='"+carerID+"';");			
						
						if(rs2.next()) {		
							out.println("<form class='pure-form pure-form-aligned' action='AccountDetails.jsp' method='post'>");
								out.println("<fieldset>");
										out.println("<input name='carerID' type='hidden' value='"+carerID+"' disabled>");
		
									out.println("<div class='pure-control-group'>");
										out.println("<label for='firstName'>First Name</label>");
										out.println("<input id='firstName' name='firstName' type='text' onblur='checkFirstName();' value='"+rs2.getString(2)+"' maxlength='20' required>");
										out.println("<span id='firstNameMessage' class='firstNameMessage'></span>");
									out.println("</div>");
									
									out.println("<div class='pure-control-group'>");
										out.println("<label for='lastName'>Last Name</label>");
										out.println("<input id='lastName' name='lastName' type='text' onblur='checkLastName();' value='"+rs2.getString(3)+"' maxlength='30' required>");
										out.println("<span id='lastNameMessage' class='lastNameMessage'></span>");
									out.println("</div>");		
		
									out.println("<div class='pure-control-group'>");
										out.println("<label for='mobile'>Mobile #</label>");
										out.println("<input id='mobile' name='mobile' type='text' onblur='checkMobile();' value='"+rs2.getString(4)+"' required>");
										out.println("<span id='mobileMessage' class='mobileMessage'></span>");
									out.println("</div>");			
		
									out.println("<div class='pure-control-group'>");
										out.println("<label for='contactNum'>Alternate Contact #</label>");
										out.println("<input id='alternateMobile' name='contactNum' type='text' onblur='checkAlternateMobile();' value='"+rs2.getString(5)+"' required>");
										out.println("<span id='alternateMobileMessage' class='alternateMobileMessage'></span>");
									out.println("</div>");	
		
									out.println("<div class='pure-control-group'>");
										out.println("<label for='email'>Email Address</label>");
										out.println("<input id='email' name='email' type='email' onblur='checkEmail();' value='"+rs2.getString(6)+"' maxlength='50' required>");
										out.println("<span id='emailMessage' class='emailMessage'></span>");
									out.println("</div>");							
		
									out.println("<input name='submitted' type='hidden' value='submitted'>");
									
									out.println("<div class='pure-controls'>");
										out.println("<button type='submit' class='pure-button pure-button-primary'>Update</button>");
									out.println("</div>");
									
								out.println("</fieldset>");
							out.println("</form>");							
						}				
						rs2.close();				
						st2.close();
						conn2.close();	
					} catch (Exception e) {
						response.sendRedirect("Error.jsp?error=9");
					}
				}					
				
			%>			
			
		</div>	
		
	<jsp:include page = "includes/footer.jsp" flush = "true" />