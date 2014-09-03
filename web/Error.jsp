<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
    <head profile="http://www.w3.org/2005/10/profile">
		<title>Error! – DementiaWatch Web Client</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link rel="icon" type="image/jpg" href="images/DementiaLogo.png">
		<script type="text/javascript">
			function goBack() {
				window.history.back()
			}
		</script>
    </head>
    <body style="font: 18pt 'Veranda', 'Goudy Old Style', sans-serif; text-align: center; background: #F0F0F0; border: 5px solid black; height: calc(100vh - 26px);">
		<h1>This is an error page – you screwed up =(</h1>
		<%	
			int errorCause;
			try { errorCause = Integer.parseInt(request.getParameter("error")); }
			catch (Exception e) { errorCause = 0; }
				
			switch (errorCause) {
				case 1:  out.println("<h3>That username does not exist.</h3>");
						 break;
				case 2:  out.println("<h3>Invalid password.</h3>");
						 break;
				case 3:  out.println("<h3>Either that username or email address is already in use.</h3>");
						 break;
				case 4:  out.println("<h3>Passwords do not match.</h3>");
						 break;
				case 5:  out.println("<h3>You are not logged in.</h3>");
						 break;
				case 6:  out.println("<h3>Someone else is using that email address.</h3>");
						 break;
				case 7:  out.println("<h3>You entered your password incorrectly.</h3>");
						 break;
				case 8:  out.println("<h3>That patientID does not exist.</h3>");
						 break;
				case 9:  //
						 break;
				case 10: //
						 break;
				case 11: //
						 break;
				case 12: //
						 break;
				default: out.println("<h3>Unknown error.........</h3>");
						 break;
			}
		%>	
		<h3><a href="javascript:goBack()">Go Back</a></h3>
    </body>
</html>

