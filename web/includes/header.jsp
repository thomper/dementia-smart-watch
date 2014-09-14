
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
						
						status = "has collapsed and may need assistance";
						
					} else if (rs.getString(4).equals("Distressed") == true) {
						rs2 = st2.executeQuery("SELECT alertDate, alertTime FROM patientalerts WHERE patientID='"+patientID+"' ORDER BY alertDate DESC, alertTime DESC");
						rs2.next();
						trigger = rs2.getDate(1).toString() + " " + rs2.getTime(2).toString();
						rs2.close();
						
						status = "has pressed the panic button and may need assistance";
					} else {
						status = "is lost and may need assistance";
					}
					
					out.println("<table class='alertsTable' align='center'>");
					out.println("<tr><td>ATTENTION!!!! &nbsp&nbsp<a href='PatientDetails.jsp?patientid=" + patientID + "'>" + rs.getString(2) + " " + rs.getString(3) + "</a> " 			+ status + " click <a href='Map.jsp?patientid=" + patientID + "'>here</a> for current location</td><td>Triggered on: " + trigger + "</td><td><a href='ChangeStatus.jsp?patientid=" + patientID + "'>Dismiss</a></td></tr>");
					out.println("</table>");
				} while (rs.next());
			%>	
		</div>
	
