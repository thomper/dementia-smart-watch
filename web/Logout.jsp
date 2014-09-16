<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head profile="http://www.w3.org/2005/10/profile">
		<title>Logout Successful – DementiaWatch Web Client</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
		<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">
		<link rel="stylesheet" type="text/css" href="css/mystyle.css">
    </head>
	
    <body>
	<div id="container">
		
		<div id="header">
			<div id="header-left">
				
			</div>
			<div id="header-right">
				
			</div>
			<div id="header-middle">
				<p>DementiaWatch Web Client</p>
			</div>			
		</div>
	
		<div id="content">
		
			<%
				session.removeAttribute("username");
				session.removeAttribute("password");
				session.removeAttribute("userid");
				session.removeAttribute("carerid");
				session.invalidate();
			%>
			<h1>You Have Successfully Logged Out</h1>
			<h2><a href="index.jsp">Back To Login</a></h2>
				
		</div>	
		
	<jsp:include page = "includes/headerC.jsp" flush = "true" />