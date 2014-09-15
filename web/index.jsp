<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head profile="http://www.w3.org/2005/10/profile">
		<title>Login – DementiaWatch Web Client</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
		<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">
		<link rel="stylesheet" type="text/css" href="css/mystyle.css">
		<%
			//If already logged in - redirect to Home
			if (session.getAttribute("userid") != null) { response.sendRedirect("Home.jsp"); }
		%>	
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
			<h1><i>Independence and Peace of Mind Matter</i></h1>
			<br>
			<h2>Please Login</h2>
				
			<form class="pure-form pure-form-stacked" action="processing/LoginCheck.jsp" method="post">
				<fieldset>

					<label for="username">User Name</label>
					<center><input name="username" type="text" placeholder="User Name" required></center>

					<label for="password">Password</label>
					<center><input name="password" type="password" placeholder="Password" required></center>

					<button type="submit" class="pure-button pure-button-primary">Sign in</button>
				</fieldset>
			</form>	
			
			<br>
			<h3>Alternatively, <a href="Register.jsp">click here</a> to create an account</h3>
				
		</div>	
		
	</div>
    </body>
</html>