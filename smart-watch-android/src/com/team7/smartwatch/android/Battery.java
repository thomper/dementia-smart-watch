package com.team7.smartwatch.android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import com.team7.smartwatch.shared.Utility;

public class Battery {

	private Context mContext;
	private int mPatientID;

	private static final String TAG = Battery.class.getName();
	private static final String SUCCESS_MESSAGE = "Battery updated\n";
	private static final String POST_URL = Globals.get().SERVER_ADDRESS
			+ "/addbattery";

	public Battery(Context context, int patientID) {

		mContext = context;
		mPatientID = patientID;
		BatteryTask task = new BatteryTask();
		task.execute(Globals.get().httpContext);
	}

	
	/* Creates a new battery with the server asynchronously. */
	public class BatteryTask extends AsyncTask<HttpContext, Void, Boolean> {
		
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
        		Log.i(TAG, "Successfully updated battery on server.");
        	} else {
        		Log.e(TAG, "Updating battery failed.");
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