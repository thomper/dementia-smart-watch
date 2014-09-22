package com.team7.smartwatch.android;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

public class AndroidUtility {
	
	public static HttpPost createJSONHttpPost(String postUrl) {
		
		HttpPost request = new HttpPost(postUrl);
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
		return request;
	}
	
	public static JSONArray JSONArrayFromHttpResponse(HttpResponse response)
			throws JSONException, IOException {
		
		return JSONArrayFromString(StringFromHttpResponse(response));
	}
	
	public static JSONArray JSONArrayFromString(String jsonString)
			throws JSONException {
		
		if (jsonString == null) {
			return null;
		}

		JSONTokener tokener = new JSONTokener(jsonString);
		return new JSONArray(tokener);
	}
	
	public static String StringFromHttpResponse(HttpResponse response)
			throws IOException {

		if (response == null) {
			return null;
		}
		
		ResponseHandler<String> handler = new BasicResponseHandler();
		return handler.handleResponse(response);
	}
}
