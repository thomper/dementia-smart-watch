package com.team7.smartwatch.android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.team7.smartwatch.shared.Utility;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

/**
 * BasicNetworkTask handles some of the boiler plate when sending a simple
 * JSON encoded HTTP POST request.
 * 
 */
public class BasicNetworkTask extends AsyncTask<HttpContext, Void, HttpResponse> {
	
	private String mLogTag;
	private List<Pair<String, Object>> mJsonList;
	private String mPostUrl;

	public BasicNetworkTask(String logTag, List<Pair<String, Object>> jsonList,
			String postUrl) {
		
		mLogTag = logTag;
		mJsonList = jsonList;
		mPostUrl = postUrl;
	}
	
	@Override
	protected HttpResponse doInBackground(HttpContext... params) {
				
		// Create request.
		HttpPost request = createRequest();
		if (request == null) {
			Log.e(mLogTag, "Error creating HTTP request.");
			return null;
		}

		// Send request.
		AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		HttpContext httpContext = params[0];
		try {
			return client.execute(request, httpContext);
		} catch (IOException e) {
			Log.e(mLogTag, Utility.StringFromStackTrace(e));
			return null;
		} finally {
			client.close();
		}
	}

    private HttpPost createRequest() {
	
			try {
				HttpPost request = new HttpPost(mPostUrl);
				request.setHeader("Accept", "application/json");
			    request.setHeader("Content-type", "application/json");
				JSONObject jObj = createJSON();
				StringEntity se = new StringEntity(jObj.toString());
				request.setEntity(se);
				return request;
			} catch (UnsupportedEncodingException e) {
				Log.e(mLogTag, Utility.StringFromStackTrace(e));
				return null;
			}
    }
    
    private JSONObject createJSON() {

		try {
			JSONObject jObj = new JSONObject();
			for (Pair<String, Object> pair : mJsonList) {
				jObj.put(pair.first, pair.second);
			}
			return jObj;
		} catch (JSONException e) {
			Log.e(mLogTag, Utility.StringFromStackTrace(e));
			return null;
		}
    }
}