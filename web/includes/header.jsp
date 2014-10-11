<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%
	 String carerID = session.getAttribute("carerid").toString();
	 String display = "none";
	 String patientID = "";

	 Class.forName("com.mysql.jdbc.Driver").newInstance();
	 java.sql.Connection conn;
	 try {
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("SELECT patientID, fName, lName, status FROM patients WHERE carerID='"+carerID+"'");
	 } catch (Exception e) {
		response.sendRedirect("assignment/Error.jsp?error=9");
	 }
%>
<script type="text/javascript">
	function changeStatus(patientID) {
		var xmlhttp;
		
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp=new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
		
		xmlhttp.onreadystatechange=function() {
			if (xmlhttp.readyState==4 && xmlhttp.status==200) {
				window.location.reload();
			}
		}
		
		xmlhttp.open("GET", "ChangeStatus.jsp?patientid="+patientID, true);
		xmlhttp.send();
	}
	
	function displayAlerts() {
		var xmlhttp;
		
		if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
			xmlhttp=new XMLHttpRequest();
		} else {// code for IE6, IE5
			xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
		}
		
		xmlhttp.onreadystatechange=function() {
			if (xmlhttp.readyState==4 && xmlhttp.status==200) {
				document.getElementById("alert").innerHTML=xmlhttp.responseText;
				setTimeout(displayAlerts, 5000);
			}
		}
		
		xmlhttp.open("GET", "AlertDisplay.jsp", true);
		xmlhttp.send();
	}
</script>
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
						<li><div><a href = 'PatientList.jsp'> My Patients </a></div></li>
						<li><div><a href = 'AccountDetails.jsp'> My Account </a></div></li>
						<li><div><a href = 'Logout.jsp'> Logout </a></div></li>
					</ul>
				</div>
			</div>
			<script type="text/javascript">displayAlerts();</script>
			<div id="alert" style="display: block;">
			</div>
		
