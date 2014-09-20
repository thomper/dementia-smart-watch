package com.team7.smartwatch.shared;

import org.json.JSONObject;

public class Patient {
	
	public Integer patientID;
	public Integer carerID;
	public String firstName;
	public String lastName;
	public Gender gender;
	public Integer age;
	public BloodType bloodType;
	public String medication;
	public PatientStatus status;
	public String homeAddress;
	public String homeSuburb;
	public String contactNum;
	public EmergencyContact emergencyContact;
	
	public Patient() {
		
	}
	
	public Patient(JSONObject jObj) {
		
		patientID = jObj.getInt("patientID");
		carerID = jObj.getInt("carerID");
		firstName = jObj.getString("firstName");
		lastName = jObj.getString("lastName");
		gender = Gender.valueOf(jObj.getString("gender"));
		age = jObj.getInt("age");
		bloodType = BloodType.valueOf(jObj.getString("bloodType"));
		medication = jObj.getString("medication");
		status = PatientStatus.valueOf(jObj.getString("status"));
		homeAddress = jObj.getString("homeAddress");
		homeSuburb = jObj.getString("homeSuburb");
		contactNum = jObj.getString("contactNum");
		
		String emPrefix = "emergencyContact";
		emergencyContact = new EmergencyContact();
		emergencyContact.name = jObj.getString(emPrefix + "Name");
		emergencyContact.address = jObj.getString(emPrefix + "Address");
		emergencyContact.suburb = jObj.getString(emPrefix + "Suburb");
		emergencyContact.num = jObj.getString(emPrefix + "Num");
	}
	
	public JSONObject toJSON() {
		
		// Everything but emergency contact.
		JSONObject jObj = new JSONObject(this, new String[] {
				"patientID",
				"carerID",
				"firstName",
				"lastName",
				"gender",
				"age",
				"bloodType",
				"medication",
				"status",
				"homeAddress",
				"homeSuburb",
				"contactNum",
		});
		
		// Add emergency contact.
		JSONObject emergencyJObj = emergencyContact.toJSON();
		for (String key : JSONObject.getNames(emergencyJObj)) {
			jObj.put(key, emergencyJObj.get(key));
		}
		
		return jObj;
	}
}