<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head profile="http://www.w3.org/2005/10/profile">
		<title>Home – DementiaWatch Web Client</title>
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
				<p>Location: Home</p>
			</div>
			<div id="header-right">
				<p><a href="ChangePassword.jsp">Change PW</a> | <a href="Logout.jsp">Logout</a></p>
			</div>
			<div id="header-middle">
				<p>DementiaWatch Web Client</p>
			</div>			
		</div>
	
		<div id="content">
		
			<h1>Main Page</h1>
			
			<img src="images/watch.png" alt="watch.png">
			<h2><a href="Map.jsp">Map</a></h2>
			
			<br>			
			<h2><a href="PatientList.jsp">My Patient List</a> &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp; <a href="AccountDetails.jsp">My Account Details</a> </h2>
				
		</div>	
		
	</div>
    </body>
</html>

