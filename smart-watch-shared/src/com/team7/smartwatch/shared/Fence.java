package com.team7.smartwatch.shared;

import org.json.JSONObject;

public class Fence {

	public Double lat;
	public Double lng;
	public Double radius;

	public JSONObject toJSON() {
		
		JSONObject jObj = new JSONObject();
		
		jObj.put( "Fencelat", lat);
		jObj.put( "FemceLng:", lng);
		jObj.put( "radius", radius);
		
		return jObj;
	}
}
