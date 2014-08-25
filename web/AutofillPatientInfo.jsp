<%@ page import="java.sql.*"%>
<%@ page import ="javax.sql.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
	String CarerID = "${sessionScope.name}";
	Boolean iterated = false;
	/* Class.forName("com.mysql.jdbc.Driver");
	java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" +
		"test","root","root"); 
	Statement st= con.createStatement(); 
	ResultSet rs=st.executeQuery("SELECT * FROM patients WHERE CarerID = '"+CarerID+"';"); 
	if(rs.next()) 
	{ 
		iterated = true;
		out.println("<FORM name='input' action='patientsProfile.php' method='get'>");
		out.println("PatientID<input id='patient' type='textbox' value='"+rs.getString(0)+"'>Uneditable<br/>");
		out.println("CarerID<input id='carer' type='textbox' value='"+rs.getString(1)+"'><br/>");
		out.println("FirstName<input id='firstName' type='textbox' value='"+rs.getString(2)+"'><br/>");
		out.println("Surname<input id='surname' type='textbox' value='"+rs.getString(3)+"'><br/>");
		out.println("Contact Number<input id='conNum' type='textbox' value='"+rs.getString(4)+"'><br/>");
		out.println("Emergency Contact Number<input id='emergNum' type='textbox' value='"+rs.getString(5)+"'><br/>");
		out.println("Unique Key<input id='uniqueKey' type='textbox' value='"+rs.getString(6)+"'><br/>");
		out.println("<input type='submit' value='Submit'>");
		out.println("</FORM>");
	} */
	
	if(!iterated){
		out.println("<FORM name='input' action='patientsProfile.php' method='get'>");
		out.println("PatientID<input id='patient' type='textbox'>Uneditable<br/>");
		out.println("CarerID<input id='carer' type='textbox'><br/>");
		out.println("FirstName<input id='firstName' type='textbox'><br/>");
		out.println("Surname<input id='surname' type='textbox'><br/>");
		out.println("Contact Number<input id='conNum' type='textbox'><br/>");
		out.println("Emergency Contact Number<input id='emergNum'><br/>");
		out.println("Unique Key<input id='uniqueKey' type='textbox'><br/>");
		out.println("<input type='submit' value='Submit'>");
		out.println("</FORM>");
	}
%>
</body>
</html>