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

import com.team7.smartwatch.shared.Patient;
import com.team7.smartwatch.shared.Utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

/** Locator provides access to the device's last known location and the
 *  time at which the location was last updated. */
public class Locator {

	private Context mContext;
	private Patient mPatient;
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	private Location mLastLocation;
	private Date mLastTime;

	private static final String TAG = Locator.class.getName();
	private static final String SUCCESS_MESSAGE = "Location updated\n";
	private static final String LOST_MESSAGE = "You are outside the safe zone\n";
	private static final String POST_URL = Globals.get().SERVER_ADDRESS
			+ "/updatelocation";

	public Locator(Context context, Patient patient) {

		mContext = context;
		mPatient = patient;
		mLocationListener = new MyLocationListener();
		mLocationManager = (LocationManager) this.mContext
				.getSystemService(Context.LOCATION_SERVICE);
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 10, mLocationListener);
	}
	
	private enum Result {
		SUCCESS, ERROR, LOST;
	}
	
	public Location getLastLocation() {

		return mLastLocation;
	}

	public Date getLastTime() {

		return mLastTime;
	}

	private class MyLocationListener implements LocationListener {
		
		@Override
		public void onLocationChanged(Location loc) {
			mLastLocation = loc;
			mLastTime = new Date();
			
			if (containsLocation(loc.getLatitude(), loc.getLongitude()) ){
				// update database
			}
						
			logLocation();
			postLocation();
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}

	/* Updates patient's current location with the server asynchronously. */
	public class LocationUpdaterTask extends AsyncTask<HttpContext, Void, Result> {
		
		@Override
        protected Result doInBackground(HttpContext... params) {

            // Create the request.
            HttpPost request = createRequest();
            if (request == null) {
                Log.e(TAG, "Error creating HTTP request");
                return Result.ERROR;
            }   

            // Send the request.
            AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
            HttpContext httpContext = params[0];
            try {
                HttpResponse response = client.execute(request, httpContext);
                return getResponseResult(response);
            } catch (IOException e) {
                Log.e(TAG, Utility.StringFromStackTrace(e));
                return Result.ERROR;
            } finally {
                client.close();
            }   
        }   

        @Override
	    protected void onPostExecute(Result result) {
	        
        	if (result == Result.SUCCESS) {
        		Log.i(TAG, "Successfully updated location on server.");
        	} else if (result == Result.LOST) {
        		Log.i(TAG, "Patient is outside the safe zone.");
        		sendTextMessage();
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
				jObj.put("patientID", mPatient.patientID);
				jObj.put("latitude", mLastLocation.getLatitude());
				jObj.put("longitude", mLastLocation.getLongitude());
				return jObj;
			} catch (JSONException e) {
				Log.e(TAG, Utility.StringFromStackTrace(e));
				return null;
			}
		}

		private Result getResponseResult(HttpResponse response) {

			try {
				String responseStr = AndroidUtility
						.StringFromHttpResponse(response);
				if (responseStr.equals(LOST_MESSAGE)) {
					return Result.LOST;
				} else if (responseStr.equals(SUCCESS_MESSAGE)) {
					return Result.SUCCESS;
				}
			} catch (IOException e) {
				Log.e(TAG, Utility.StringFromStackTrace(e));
			}
			
			return Result.ERROR;
		}
	}

	private void logLocation() {

		String latitude = "Latitude: "
				+ String.valueOf(mLastLocation.getLatitude());
		String longitude = "Longitude: "
				+ String.valueOf(mLastLocation.getLongitude());
		String timeGMT = "GMT Time: " + dateToGMTString(mLastTime);
		Log.v(TAG, latitude + ", " + longitude + ", " + timeGMT);
	}

	private void postLocation() {
		
		LocationUpdaterTask task = new LocationUpdaterTask();
		task.execute(Globals.get().httpContext);
	}
	
	@SuppressLint("SimpleDateFormat")
	private String dateToGMTString(Date time) {

		SimpleDateFormat dfGMT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		dfGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dfGMT.format(time);
	}

	public void sendTextMessage() {
		
		SmsManager.getDefault().sendTextMessage(mPatient.emergencyContact.num,
				null, mPatient.fullName() + " has become lost!",
				null, null);
	}
}