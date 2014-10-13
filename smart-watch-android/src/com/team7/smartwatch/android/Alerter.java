package com.team7.smartwatch.android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import com.team7.smartwatch.android.Locator.LocationUpdaterTask;
import com.team7.smartwatch.shared.Utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/** Locator provides access to the device's last known location and the
 *  time at which the location was last updated. */
public class Alerter {

	private Context mContext;
	private int mPatientID;

	private static final String TAG = Alerter.class.getName();
	private static final String SUCCESS_MESSAGE = "Location updated\n";
	private static final String POST_URL = Globals.get().SERVER_ADDRESS
			+ "/addalert";

	public Alerter(Context context, int patientID) {

		mContext = context;
		mPatientID = patientID;
		AlerterTask task = new AlerterTask();
		task.execute(Globals.get().httpContext);
	}

	
	/* Creates a new alert with the server asynchronously. */
	public class AlerterTask extends AsyncTask<HttpContext, Void, Boolean> {
		
		@Override
        protected Boolean doInBackground(HttpContext... params) {

            // Create the request.
            HttpPost request = createRequest();
            if (request == null) {
                Log.e(TAG, "Error creating HTTP request");
                return false;
            }   

            // Send the request.
            AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
            HttpContext httpContext = params[0];
            try {
                HttpResponse response = client.execute(request, httpContext);
                return responseSucceeded(response);
            } catch (IOException e) {
                Log.e(TAG, Utility.StringFromStackTrace(e));
                return false;
            } finally {
                client.close();
            }   
        }   

        @Override
	    protected void onPostExecute(Boolean succeeded) {
	        
        	if (succeeded) {
        		Log.i(TAG, "Successfully updated location on server.");
        	} else {
        		Log.e(TAG, "Updating location failed.");
        	}
	    }

		private HttpPost createRequest() {
			
			HttpPost request = AndroidUtility
					.createJSONHttpPost(POST_URL);
			
			// Create JSON.
			JSONObject jObj = createJSON();
			if (jObj == null) {
				return null;
			}
			
			// Add JSON to request.
			try {
				StringEntity stringEntity = new StringEntity(jObj.toString());
				request.setEntity(stringEntity);
				return request;
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, Utility.StringFromStackTrace(e));
				return null;
			}
		}
		
		private JSONObject createJSON() {
			
			try {
				JSONObject jObj = new JSONObject();
				jObj.put("patientID", mPatientID);
				return jObj;
			} catch (JSONException e) {
				Log.e(TAG, Utility.StringFromStackTrace(e));
				return null;
			}
		}

		private boolean responseSucceeded(HttpResponse response) {

			try {
				String responseStr = AndroidUtility
						.StringFromHttpResponse(response);
				return responseStr.equals(SUCCESS_MESSAGE);
			} catch (IOException e) {
				Log.e(TAG, Utility.StringFromStackTrace(e));
				return false;
			}
		}
	}
}