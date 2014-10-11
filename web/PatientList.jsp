<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html>

<html>
    <head profile="http://www.w3.org/2005/10/profile">
		<title>My Patient List – DementiaWatch Web Client</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
		<link rel="stylesheet" type="text/css" href="css/mystyle.css">
		<%
			//If not logged in - redirect to error page and cancel processing of remaining jsp
			if (session.getAttribute("userid") == null) { response.sendRedirect("Error.jsp?error=5"); return; }
			
			String carerID = session.getAttribute("carerid").toString();
		%>			
    <%-- This will add the rest of the head tag and navigation and alerts --%>	
	<jsp:include page = "includes/header.jsp" flush = "true" />
	<jsp:include page = "includes/headerP.jsp" flush = "true" />
	
		<div id="content">
			
			<h2>My Patient List</h2>
			
			<%
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				java.sql.Connection conn2;
				
				try {
					conn2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
					Statement st2 = conn2.createStatement();
					ResultSet rs3 = st2.executeQuery("SELECT patientID, fName, lName, status FROM patients WHERE carerID='"+carerID+"'");	
	
						out.println("<table align='center'>");
						out.println("<tr>");
							out.println("<td> Name </td> <td> Location </td> <td> Change Details </td> <td> Delete </td> ");
						out.println("</tr>");
						
					while (rs3.next()) {
						String patientID = rs3.getString(1);
						if (rs3.getString(4).equals("FINE")) { 
							out.println("<tr style='background-colour: green;'>");
						} else { 
							out.println("<tr style>");
						}
						out.println("<td>"+rs3.getString(2)+" "+rs3.getString(3)+" </td><td> <a href='Map.jsp?patientid="+
							patientID+"'>Location</a> </td><td> <a href='PatientDetails.jsp?patientid="+patientID+"'>Change Details</a> </td><td><a href='DeletePatient.jsp?patientid=" + patientID + "'> Delete Patient</a></td>");
						out.println("</tr>");					
					}
					out.println("</table>");
				} catch (Exception e) {
					response.sendRedirect("../Error.jsp?error=9");
				}
		
			%>	
		</div>	
	<jsp:include page = "includes/footer.jsp" flush = "true" />
