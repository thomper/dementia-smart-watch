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
		

		// Built in types first.
		patientID = jObj.getInt("patientID");
		carerID = jObj.getInt("carerID");
		firstName = jObj.getString("firstName");
		lastName = jObj.getString("lastName");
		age = jObj.getInt("age");
		medication = jObj.getString("medication");
		homeAddress = jObj.getString("homeAddress");
		homeSuburb = jObj.getString("homeSuburb");
		contactNum = jObj.getString("contactNum");

		// Enums.
		// TODO: this was a quick hack for the second progress report.
		if (jObj.has("gender")) {
			gender = Gender.valueOf(jObj.getString("gender"));
		} else {
			gender = null;
		}

		if (jObj.has("bloodType")) {
			bloodType = BloodType.valueOf(jObj.getString("bloodType"));
		} else {
			bloodType = null;
		}
		
		if (jObj.has("status")) {
			status = PatientStatus.valueOf(jObj.getString("status"));
		} else {
			status = null;
		}
		
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