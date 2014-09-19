<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head profile="http://www.w3.org/2005/10/profile">
		<title>Register – DementiaWatch Web Client</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
		<link rel="stylesheet" href="http://yui.yahooapis.com/pure/0.5.0/pure-min.css">
		<link rel="stylesheet" type="text/css" href="css/mystyle.css">
		<script src="scripts/register.js" type="text/javascript"></script>
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
					<center><input id="firstName" name="fname" type="text" placeholder="First Name" onblur="checkFirstName();" maxlength='20' required>
					<span id="firstNameMessage" class="firstNameMessage"></span></center>
					
					<label for="username">Last Name</label>
					<center><input id="lastName" name="lname" type="text" placeholder="Last Name" onblur="checkLastName(); return false;" maxlength='30' required>
					<span id="lastNameMessage" class="lastNameMessage"></span></center>
					
					<label for="username">Mobile #</label>
					<center><input id="mobile" name="mobile" type="number" onblur="checkMobile(); return false;" placeholder="0412345678" maxlength='10' required>
					<span id="mobileMessage" class="mobileMessage"></span></center>
					
					<label for="username">Alternate Contact #</label>
					<center><input id="alternateMobile" name="contactnum" type="number" onblur="checkAlternateMobile(); return false;" placeholder="0712345678" maxlength='10' required>
					<span id="alternateMobileMessage" class="alternateMobileMessage"></span></center>
					
					<legend><b>Account Details:</b></legend>
					
					<label for="email">Email Address</label>
					<center><input id="email" name="email" type="email" placeholder="foo@bar.com" onblur="checkEmail(); return false;" maxlength='50' required>
					<span id="emailMessage" class="emailMessage"></span></center>
					
					<label for="username">User Name</label>
					<center><input id="username" name="username" type="text" placeholder="Prefered Username" onblur="checkUsername(); return false;" maxlength='15' required>
					<span id="usernameMessage" class="usernameMessage"></span></center>

					<label for="password">Password</label>
					<center><input id="password" name="password" type="password" placeholder="Password" onblur="checkPassword(); return false;" maxlength='20' required>
					<span id="passwordMessage" class="passwordMessage"></span></center>
					
					<label for="password">Confirm Password</label>
					<center><input id="confirmPassword" name="confirmpassword" type="password" placeholder="Password Again" onblur="checkConfirmPassword(); return false;" maxlength='20' required>
					<span id="confirmPasswordMessage" class="confirmPasswordMessage"></span><center>

					<button type="submit" name="register" class="pure-button pure-button-primary" value="register">Create Account!</button>
				</fieldset>
			</form>		
			
			<br>
			<h4><a href="index.jsp">Back To Login<a></h4>
					
		</div>	
	
	<jsp:include page = "includes/headerC.jsp" flush = "true" />