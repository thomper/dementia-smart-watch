package com.team7.smartwatch.shared;

import org.json.JSONObject;

public class EmergencyContact {

	public String name;
	public String address;
	public String suburb;
	public String num;
	
	public JSONObject toJSON() {
		
		JSONObject jObj = new JSONObject();
		String prefix = "emergencyContact";
		
		jObj.put(prefix + "Name", name);
		jObj.put(prefix + "Address", address);
		jObj.put(prefix + "Suburb", suburb);
		jObj.put(prefix + "Num", num);
		
		return jObj;
	}
}
