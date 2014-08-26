<%@ page import="java.sql.*"%>
<%@ page import ="javax.sql.*" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" session="true"%> <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/normalize.css">
<link rel="stylesheet" type="text/css" href="css/style.css">
<title>dis ma title dad</title>
</head>
<body>
	<div id = "container">
		<div id = "header">
			<div id = "logo">
				<h1> Dementia <span id = "blue">Assistant </span></h1>
				<!--<h2> Independence and Peace of Mind Matters </h2>-->
			</div>
			<div id = "nav">
				<ul>
					<li> <a href = 'index.jsp'> Home </a></li>
					<li> <a href = 'about.jsp'> What DA Does</a></li>
					<% if (true){%>
						<!--<li> <a href = 'location.jsp'> Patient's <br/> Location </a></li>
						<li> <a href = 'health.jsp'> Patient's <br/> Health</a></li>
						<li> <a href = 'schedule.jsp'> Patient's <br/>Schedule </a></li>-->
						<li> <a href = 'account.jsp'> My Patient's </a></li>
						<li> <a href = 'account.jsp'> My Account </a></li>
						<li><a href = 'logout.jsp'> Logout </a></li>					
					<% } else {%>										
						<li><a href = 'login.jsp'> Login </a></li>
						<% } %>
				</ul>
			</div> <!-- end nav -->
		</div> <!-- end header -->
		<div id = "content">
			lorem ipsum<br/><br/><br/>testatesta testa <br/>
		</div>

	</div> <!-- end container -->
</body>

</html>