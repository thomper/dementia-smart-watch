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
	if(request.getParameter("carer")!=null && request.getParameter("patient")!=null){
	Class.forName("com.mysql.jdbc.Driver").newInstance(); 
	Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" +
	"dementiawatch_db","root",""); 
	Statement st= con.createStatement();
	st.executeUpdate("UPDATE patients SET carerID = '"+ request.getParameter("carer")+"' WHERE patientID = '"+request.getParameter("patient")+"'"); 
	out.println("<H1>Success</H1>");
	}
%>

Link Patient to carer
<FORM name='input' action='AddPatientsToCarers.jsp' method='post'>
	Patient<input name='patient' type='textbox'/>
	Carer<input name='carer' type='textbox'/>
	<input type="submit" value="Submit">
</FORM>
</body>
</html>