<!DOCTYPE html>
<html>
    <head profile="http://www.w3.org/2005/10/profile">
		<title>Home – DementiaWatch Web Client</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
		<link rel="stylesheet" type="text/css" href="css/mystyle.css">
		<%
			// If not logged in - redirect to error page and cancel processing od remaining jsp
			if (session.getAttribute("userid") == null) { response.sendRedirect("Error.jsp?error=5"); return; }
		%>			
    </head>
	<body>
	<div id="container">	
		<div id="header">
			<div id = "Title">
				<h1> Dementia <span id = "blue">Assistant </span></h1>
				<!--<h2> Independence and Peace of Mind Matters </h2>-->
			</div>	
			<div id="nav"> 
				<ul>
					<li><div><a href = 'index.jsp'> Home </a></div></li>
					<li><div><a href = 'PatientList.jsp.jsp'> My Patients </a></div></li>
					<li><div><a href = 'AccountDetails.jsp'> My Account </a></div></li>
					<li><div><a href = 'logout.jsp'> Logout </a></div></li>
				</ul>
			</div>
		</div>
		<div id="alert">
	</div>
	