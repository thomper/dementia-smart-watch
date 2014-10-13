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
		
		while (rs.next()) {	
			if (rs.getString(4).equals("FINE") == false) {
				patientID = rs.getString(1);
				Statement st2 = conn.createStatement();
				ResultSet rs2 = null;
				
				String status = "";
				String trigger = "";
				
				if (rs.getString(4).equals("FALLEN") == true) {
					rs2 = st2.executeQuery("SELECT collapseDate, collapseTime FROM patientcollapses WHERE patientID='"+patientID+"' ORDER BY collapseDate DESC, collapseTime DESC");
					rs2.next();
					trigger = rs2.getDate(1).toString() + " " + rs2.getTime(2).toString();
					rs2.close();
					
					status = "has collapsed and may need assistance.";
					
				} else if (rs.getString(4).equals("DISTRESSED") == true) {
					rs2 = st2.executeQuery("SELECT alertDate, alertTime FROM patientalerts WHERE patientID='"+patientID+"' ORDER BY alertDate DESC, alertTime DESC");
					rs2.next();
					trigger = rs2.getDate(1).toString() + " " + rs2.getTime(2).toString();
					rs2.close();
					
					status = "has pressed the panic button and may need assistance.";
				} else if (rs.getString(4).equals("BATTERY_LOW") == true) {
					rs2 = st2.executeQuery("SELECT alertDate, alertTime, batteryLevel FROM patientbatteryalerts WHERE patientID = '"+patientID+"' ORDER BY alertDate DESC, alertTime DESC");
					rs2.next();
					trigger = rs2.getDate(1).toString() + " " + rs2.getTime(2).toString();
					
					status = "devices battery is low and needs charging, current level: " + rs2.getString(3);
					rs2.close();
				} else if (rs.getString(4).equals("LOST") == true) {
					rs2 = st2.executeQuery("SELECT alertDate, alertTime FROM patientalerts WHERE patientID='"+patientID+"' ORDER BY alertDate DESC, alertTime DESC");
					rs2.next();
					trigger = rs2.getDate(1).toString() + " " + rs2.getTime(2).toString();
					rs2.close();
					status = "is lost and may need assistance.";
				}
				
				out.println("<img src='images/exclamation2.gif' /></img>");
				out.println("<a href='PatientDetails.jsp?patientid=" + patientID + "'>" + rs.getString(2) + " " + rs.getString(3) + "</a> " + status + "  <a href='Map.jsp?patientid=" + patientID + "'>View Location</a> Logged at: " + trigger + " <a href='javascript:changeStatus(" + patientID + ")'>Dismiss</a>");
				out.println("<br/>");
				st2.close();
			}		
		}	
		rs.close();
		st.close();
		conn.close();
	} catch (Exception e) {
		out.println("An error has occured and connection has failed, please refresh the page and try again");
	}
%>