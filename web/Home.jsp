<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	<head profile="http://www.w3.org/2005/10/profile">
		<title> Home - Dementia Web Client</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
		<link rel="stylesheet" type="text/css" href="css/mystyle.css">
		<%
			// If not logged in - redirect to error page and cancel processing od remaining jsp
			if (session.getAttribute("userid") == null) { response.sendRedirect("Error.jsp?error=5"); return; }
		%>			
	</head>

	<jsp:include page = "includes/header.jsp" flush = "true" />	
				
		<div id="content">
		
			<h1>Main Page</h1>
			
			<img src="images/watch.png" alt="watch">
			
			<br/>
				
		</div> <!-- content -->	
	
	<jsp:include page = "includes/footer.jsp" flush = "true" />
    
