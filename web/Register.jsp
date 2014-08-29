<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head profile="http://www.w3.org/2005/10/profile">
		<title>Register – DementiaWatch Web Client</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
		<link rel="stylesheet" type="text/css" href="css/mystyle.css">
		<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">
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
			<h1>Create An Account</h1>
			
			<form class="pure-form pure-form-stacked" action="processing/RegisterCheck.jsp" method="post">
				<fieldset>
					<legend><b>Your Details:</b></legend>
					
					<label for="username">First Name</label>
					<center><input name="fname" type="text" placeholder="First Name" required></center>
					
					<label for="username">Last Name</label>
					<center><input name="lname" type="text" placeholder="Last Name" required></center>
					
					<label for="username">Mobile #</label>
					<center><input name="mobile" type="text" placeholder="0412345678" required></center>
					
					<label for="username">Alternate Contact #</label>
					<center><input name="contactnum" type="text" placeholder="0712345678" required></center>
					
					<legend><b>Account Details:</b></legend>
					
					<label for="email">Email Address</label>
					<center><input name="email" type="email" placeholder="foo@bar.com" required></center>
					
					<label for="username">User Name</label>
					<center><input name="username" type="text" placeholder="Prefered Username" required></center>

					<label for="password">Password</label>
					<center><input name="password" type="password" placeholder="Password" required></center>
					
					<label for="password">Confirm Password</label>
					<center><input name="confirmpassword" type="password" placeholder="Password Again" required><center>

					<button type="submit" class="pure-button pure-button-primary">Create Account!</button>
				</fieldset>
			</form>	
			
			<br>
			<h4><a href="index.jsp">Back To Login<a></h4>
					
		</div>	
		
	</div>
    </body>
</html>