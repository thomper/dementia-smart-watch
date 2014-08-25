<%@ page import="java.sql.*"%>
<%@ page import="java.io.*" %> 
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Link Patients to Carers - Project Dementia Watch</title>
</head>
<body>
<% 
	Class.forName("com.mysql.jdbc.Driver").newInstance(); 
	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" +
	"dementiawatch_db","root",""); 
	Statement st= con.createStatement(); 
	st.executeQuery("UPDATE patients SET carerID = '"+ request.getParameter("carer")+"' WHERE patientID = '"+request.getParameter("patient")+"'"); 
	out.println("<H1>Success</H1>"); */
%>

Link Patient to carer
<FORM name='input' action='AddPatientsToCarers.jsp' method='post'>
	Patient<input id='patient' type='textbox'>
	Carer<input id='carer' type='textbox'>
	<input type="submit" value="Submit">
</FORM>
</body>
</html>