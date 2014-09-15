<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
	 String carerID = session.getAttribute("carerid").toString();
	 String display = "none";
	 String patientID = "";

	 Class.forName("com.mysql.jdbc.Driver").newInstance();
	 java.sql.Connection conn;
	 conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dementiawatch_db?user=agile374&password=dementia374");
	 Statement st = conn.createStatement();
	 ResultSet rs = st.executeQuery("SELECT patientID, fName, lName, status FROM patients WHERE carerID='"+carerID+"'");

	 if (rs.next()) {
		if (rs.getString(4).equals("fine") == false) {
			display = "block";
		}
	}
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
						<li><div><a href = 'PatientList.jsp'> My Patients </a></div></li>
						<li><div><a href = 'AccountDetails.jsp'> My Account </a></div></li>
						<li><div><a href = 'Logout.jsp'> Logout </a></div></li>
					</ul>
				</div>
			</div>
			<div id="alert" style="display: <%=display%>;">
				<%		
					do {
						patientID = rs.getString(1);
						Statement st2 = conn.createStatement();
						ResultSet rs2 = null;
						
						String status = "";
						String trigger = "";
						
						if (rs.getString(4).equals("Collapsed") == true) {
							rs2 = st2.executeQuery("SELECT collapseDate, collapseTime FROM patientcollapses WHERE patientID='"+patientID+"' ORDER BY collapseDate DESC, collapseTime DESC");
							rs2.next();
							trigger = rs2.getDate(1).toString() + " " + rs2.getTime(2).toString();
							rs2.close();
							
							status = "has collapsed and may need assistance.";
							
						} else if (rs.getString(4).equals("Distressed") == true) {
							rs2 = st2.executeQuery("SELECT alertDate, alertTime FROM patientalerts WHERE patientID='"+patientID+"' ORDER BY alertDate DESC, alertTime DESC");
							rs2.next();
							trigger = rs2.getDate(1).toString() + " " + rs2.getTime(2).toString();
							rs2.close();
							
							status = "has pressed the panic button and may need assistance.";
						} else {
							status = "is lost and may need assistance.";
						}
						
						out.println("<img src='images/exclamation2.gif' />");
						out.println("<a href='PatientDetails.jsp?patientid=" + patientID + "'>" + rs.getString(2) + " " + rs.getString(3) + "</a> " + status + "  <a href='Map.jsp?patientid=" + patientID + "'>View Location</a> Logged at: " + trigger + " <a href='ChangeStatus.jsp?patientid=" + patientID + "'>Dismiss</a>");
						out.println("<br/>");
					} while (rs.next());
				%>	
			</div>
		
