package com.team7.smartwatch.shared;

import java.util.HashMap;
import java.util.Map;

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
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("patientID", patientID);
		map.put("carerID", carerID);
		map.put("firstName", firstName);
		map.put("lastName", lastName);
		map.put("age", age);
		map.put("medication", medication);
		map.put("homeAddress", homeAddress);
		map.put("homeSuburb", homeSuburb);
		map.put("contactNum", contactNum);

		// Enums.
		map.put("gender", gender.toString());
		map.put("bloodType", bloodType.toString());
		map.put("status", status.toString());

		String emPrefix = "emergencyContact";
		map.put(emPrefix + "Name", emergencyContact.name);
		map.put(emPrefix + "Address", emergencyContact.address);
		map.put(emPrefix + "Suburb", emergencyContact.suburb);
		map.put(emPrefix + "Num", emergencyContact.num);

		return new JSONObject(map);
	}
	
	public String fullName() {

		if (firstName == null || firstName.length() < 1) {
            return lastName;
        }
		if (lastName == null || lastName.length() < 1) {
			return firstName;
		}

		return firstName + " " + lastName;
	}
}