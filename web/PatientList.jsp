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
			String display = "none";
			String patientID = "";
			
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			java.sql.Connection conn;
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT patientID, fName, lName, status FROM patients WHERE carerID='"+carerID+"'");

			if (rs.next()) {
				if (rs.getString(4).equals("fine") == false) {
					display = "inline";
				}
			}
		%>			
    <%-- This will add the rest of the head tag and navigation and alerts --%>	
	<jsp:include page = "includes/header.jsp" flush = "true" />
				
		<div id="header2">
			<div id="header-left">
				<p>Location: <a href="Home.jsp">Home</a> > My Patient List</p>
			</div>
			<div id="header-right">
				<p><a href="ChangePassword.jsp">Change PW</a> | <a href="Logout.jsp">Logout</a></p>
			</div>
			<div id="header-middle">
				<p>DementiaWatch Web Client</p>
			</div>			
		</div>
	
		<div id="content">
			
			<h1>My Patient List</h1>
			<h3><a href="AddPatient.jsp">Add New Patient</a></h3>
			
			<%
				rs = st.executeQuery("SELECT patientID, fName, lName, status FROM patients WHERE carerID='"+carerID+"'");	

								out.println("<table align='center'>");
					out.println("<tr>");
						out.println("<td> Name </td> <td> Location </td> <td> Change Details </td> <td> Delete </td> ");
					out.println("</tr>");

				while (rs.next()) {
					patientID = rs.getString(1);
					if (rs.getString(4).equals("fine") == false) { 
						out.println("<tr style='background-colour: red;'>");
					} else { 
						out.println("<tr style>");
					}
					out.println("<td>PatientName: "+rs.getString(2)+" "+rs.getString(3)+" </td><td> <a href='Map.jsp?patientid="+
						patientID+"'>Location</a> </td><td> <a href='PatientDetails.jsp?patientid="+patientID+"'>Change Details</a> </td><td><a href='DeletePatient.jsp?patientid=" + patientID + "'> Delete Patient</a></td>");
					out.println("</tr>");					
				}
				out.println("</table>");
		
			%>
			
			<!--<br><br><br>
			<p>The above needs to be put in a nice table, ideally status would not be displayed as text, but if a patients status is not OK then their row in the table would be red.</p>
			<p>Deleting patients implemented - needs to work with AJAX (doesn't currently, I'll fix another time if someone else doesn't)</p> -->
				
		</div>	
		
<jsp:include page = "includes/footer.jsp" flush = "true" />	


