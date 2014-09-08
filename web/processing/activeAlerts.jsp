<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>

<%
	if(request.getAttribute("carer")!=null){
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		java.sql.Connection conn;
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
		Statement st = conn.createStatement();
		st.executeUpdate("UPDATE patients set status = 'fine' WHERE carerID = '"+request.getAttribute("carer")+"'");
		st.close();
		conn.close();
		out.println("<H3>Clear Successful</H3>");
	}


	String carerID = session.getAttribute("carerid").toString();
	Class.forName("com.mysql.jdbc.Driver").newInstance();
	java.sql.Connection conn;
	conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
	Statement st = conn.createStatement();
	
	ResultSet rs = st.executeQuery("SELECT fName, lName, status FROM patients WHERE carerID='"+carerID+"' & status!='fine';");
	
	out.println("<table><tr><th>FirstName</th><th>Last Name</th><th>status</th>");
	
	while(rs.next()){
		out.println("<tr><td>");
		out.println(rs.getString(1));
		out.println("</td><td>");
		out.println(rs.getString(2));
		out.println("</td><td>");
		out.println(rs.getString(3));
		out.println("</td></tr>");
	}
	out.println("</table>");
	out.println("<form class='pure-form pure-form-aligned' method='POST' action='Home.jsp'>");
	out.println("<input type='hidden' name='carer' value='"+session.getAttribute("carerid")+"'>");
	out.println("<submit type='submit'>");
	out.println("</form>");
	rs.close();
	st.close();
	conn.close();
%>