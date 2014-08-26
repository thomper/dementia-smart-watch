<%@ page import="java.sql.*"%>
<%@ page import="java.io.*" %> 
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<FORM name='input' action='AutofillPatientInfo.jsp' method='get'>
Enter CarerID:<input name='careid' type='textbox'>
<input type='submit' value='Submit'>
</FORM>
<%
	if(request.getParameter("careid")!=null){
		//String CarerID = request.getParameter("sessionid");
		String CarerID = "1";
		boolean iterated = false;
		Class.forName("com.mysql.jdbc.Driver");
		java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" +
			"dementiawatch_db","root",""); 
		Statement st= con.createStatement(); 
		
		//If forms been submitted with values
		if(request.getParameter("patientID")!=null&&request.getParameter("firstName")!=null&&request.getParameter("surname")!=null&&request.getParameter("conNum")!=null&&
				request.getParameter("emergNum")!=null&&request.getParameter("uniqueKey")!=null){
			//one massive sql update statement
			st.executeUpdate("UPDATE patients set carerID='"+request.getParameter("carer")+"', fName='"+request.getParameter("firstName")+"', "+
				"gender='"+request.getParameter("gender")+"', age='"+request.getParameter("age")+"', bloodType='"+request.getParameter("bloodType")+"', "+
				"status='"+request.getParameter("status")+"', homeAddress='"+request.getParameter("address")+"', "+
				"homeSuburb='"+request.getParameter("suburb")+"', emergencyContactName='"+request.getParameter("emergName")+"', "+
				"emergencyContactAddress='"+request.getParameter("emergAddress")+"', emergencyContactSuburb='"+request.getParameter("emergSuburb")+"', "+
				"lName='"+request.getParameter("surname")+"', contactNum = '"+request.getParameter("conNum")+"', emergencyContactNum = '"+
						request.getParameter("emergNum")+"', uniqueKey = '"+request.getParameter("uniqueKey")+
						"' WHERE patientID = '"+request.getParameter("patientID")+"';");
		}
		
		ResultSet rs=st.executeQuery("SELECT * FROM patients WHERE CarerID = '"+CarerID+"';"); 
		if(rs.next()) 
		{ 
			iterated = true;
			out.println("<FORM name='input' action='AutofillPatientInfo.jsp' method='get'>");
			out.println("<input type='hidden' name='patientID' value='"+rs.getString(1)+"'>");
			out.println("PatientID "+rs.getString(1)+"<br/>");
			out.println("CarerID<input name='carer' type='textbox' value='"+rs.getString(2)+"'><br/>");
			out.println("FirstName<input name='firstName' type='textbox' value='"+rs.getString(3)+"'><br/>");
			out.println("Surname<input name='surname' type='textbox' value='"+rs.getString(4)+"'><br/>");
			if(rs.getString(5)=="Male"){
				out.println("Gender<select name='gender'><option value='Male' selected>Male</option>"+
			"<option value='Female'>Female</option></select><br/>");
			}else{
				out.println("Gender<select name='gender'><option value='Male'>Male</option>"+
			"<option value='Female' selected>Female</option></select><br/>");
			}
			out.println("Age<input name='age' type='textbox' value='"+rs.getString(6)+"'><br/>");
			out.println("Blood Type<input name='bloodType' type='textbox' value='"+rs.getString(7)+"'><br/>");
			out.println("Status<input name='status' type='textbox' value='"+rs.getString(8)+"'><br/>");
			out.println("home Address<input name='address' type='textbox' value='"+rs.getString(9)+"'><br/>");
			out.println("home Suburb<input name='suburb' type='textbox' value='"+rs.getString(10)+"'><br/>");
			out.println("Contact Number<input name='conNum' type='textbox' value='"+rs.getString(11)+"'><br/>");
			out.println("Emergency contact Name<input name='emergName' type='textbox' value='"+rs.getString(12)+"'><br/>");
			out.println("Emergency contact Address<input name='emergAddress' type='textbox' value='"+rs.getString(13)+"'><br/>");
			out.println("Emergency contact Suburb<input name='emergSuburb' type='textbox' value='"+rs.getString(9)+"'><br/>");
			out.println("Emergency Contact Number<input name='emergNum' type='textbox' value='"+rs.getString(15)+"'><br/>");
			out.println("Unique Key<input name='uniqueKey' type='textbox' value='"+rs.getString(16)+"'><br/>");
			out.println("<input type='submit' value='Submit'>");
			out.println("</FORM>");
		}
	
	}
%>
</body>
</html>