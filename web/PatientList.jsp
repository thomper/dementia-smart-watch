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
			//If not logged in - redirect to error page and cancel processing od remaining jsp
			if (session.getAttribute("userid") == null) { response.sendRedirect("Error.jsp?error=5"); return; }
		%>			
    </head>
	
    <body>
	<div id="container">
		
		<div id="header">
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
				String carerID = session.getAttribute("carerid").toString();
				String patientID = "";
			
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				java.sql.Connection conn;
				conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=root");
				Statement st = conn.createStatement();
				ResultSet rs = st.executeQuery("SELECT patientID, fName, lName, status FROM patients WHERE carerID='"+carerID+"'");	

				while (rs.next()) {
					patientID = rs.getString(1);
					out.println("<p>PatientName: "+rs.getString(2)+" "+rs.getString(3)+" | Status: "+rs.getString(4)+" | <a href='Map.jsp?patientid="+
						patientID+"'>Location</a> | <a href='PatientDetails.jsp?patientid="+patientID+"'>Change Details</a> | Delete Patient</p>");
					
				}
			%>
			
			<br><br><br>
			<p>The above needs to be put in a nice table, ideally status would not be displayed as text, but if a patients status is not OK then their row in the table would be red.</p>
			<p>Deleting patients not yet implemented. If someone wants to do this note:- patients can't exist without a carer, they are automatically assigned to the carer that created them</p>
				
		</div>	
		
	</div>
    </body>
</html>

