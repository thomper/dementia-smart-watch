package com.team7.smartwatch.android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

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
public class Locator {

	private Context mContext;
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	private Location mLastLocation;
	private Date mLastTime;
	private int mPatientID;
	private static String mPostUrl;

	private static final String SUCCESS_MESSAGE = "Location updated\n";
	private static final String TAG = Locator.class.getName();

	public Locator(Context context, int patientID, String serverAddress) {
		this.mContext = context;
		this.mPatientID = patientID;
		mPostUrl = serverAddress + "/updatelocation";
		mLocationListener = new MyLocationListener();
		mLocationManager = (LocationManager) this.mContext
				.getSystemService(Context.LOCATION_SERVICE);
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 10, mLocationListener);
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

	public class LocationUpdaterTask extends
			AsyncTask<Void, Void, Boolean> {
			
		@Override
		protected Boolean doInBackground(Void... params) {
					
			// Create request.
			HttpPost request = createRequest();
			if (request == null) {
				Log.e(TAG, "Error creating HTTP request.");
				return false;
			}

			// Send request.
			AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
			try {
				HttpResponse response = client.execute(request);
				return responseSucceeded(response);
			} catch (IOException e) {
				Log.e(TAG, Utility.StringFromStackTrace(e));
				return false;
			} finally {
				client.close();
			}
		}
	
	    @Override
	    protected void onPostExecute(Boolean result) {
	        
	        if (result != null) {
	        	if (result == false) {
	        		Log.e(TAG, "Updating location failed.");
	        	}
	        }
	    }
	    
	    /* Create a HTTP POST request with location update details in JSON. */
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
					Log.e(TAG, Utility.StringFromStackTrace(e));
					return null;
				}
	    }
	    
	    private JSONObject createJSON() {

			try {
				JSONObject jObj = new JSONObject();
				jObj.put("patientID", mPatientID);
				jObj.put("latitude", mLastLocation.getLatitude());
				jObj.put("longitude", mLastLocation.getLongitude());
				return jObj;
			} catch (JSONException e) {
				Log.e(TAG, Utility.StringFromStackTrace(e));
				return null;
			}
	    }

		private boolean responseSucceeded(HttpResponse response) {

			try {
				String responseStr = EntityUtils.toString(response.getEntity());
				return responseStr.equals(SUCCESS_MESSAGE);
			} catch (IOException e) {
				Log.e(TAG, Utility.StringFromStackTrace(e));
				return false;
			}
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
		
            new LocationUpdaterTask().execute();
	}
	
	@SuppressLint("SimpleDateFormat")
	private String dateToGMTString(Date time) {

		SimpleDateFormat dfGMT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		dfGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dfGMT.format(time);
	}
}
