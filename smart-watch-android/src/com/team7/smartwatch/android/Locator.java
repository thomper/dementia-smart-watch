package com.team7.smartwatch.android;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.team7.smartwatch.shared.Utility;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

/** Locator provides access to the device's last known location and the
 *  time at which the location was last updated. */
public class Locator {

	private Context mContext;
	private int mPatientID;
	private String mPostUrl;
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	private Location mLastLocation;
	private Date mLastTime;

	private static final String SUCCESS_MESSAGE = "Location updated\n";
	private static final String TAG = Locator.class.getName();

	public Locator(Context context, int patientID) {

		mContext = context;
		mPatientID = patientID;
		mPostUrl = Globals.get().SERVER_ADDRESS + "/updatelocation";
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

	public class LocationUpdaterTask extends BasicNetworkTask {
			
		public LocationUpdaterTask(String logTag,
				List<Pair<String, Object>> jsonList, String postUrl) {
			super(logTag, jsonList, postUrl);
		}

	    protected void onPostExecute(HttpResponse result) {
	        
        	if (!responseSucceeded(result)) {
        		Log.e(TAG, "Updating location failed.");
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
		
		List<Pair<String, Object>> jsonList =
				new ArrayList<Pair<String, Object>>();
		jsonList.add(Pair.create("patientID", (Object)mPatientID));
		jsonList.add(Pair.create("latitude",
				(Object) mLastLocation.getLatitude()));
		jsonList.add(Pair.create("longitude",
				(Object) mLastLocation.getLongitude()));

		new LocationUpdaterTask(TAG, jsonList, mPostUrl)
				.execute(Globals.get().httpContext);
	}
	
	@SuppressLint("SimpleDateFormat")
	private String dateToGMTString(Date time) {

		SimpleDateFormat dfGMT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		dfGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dfGMT.format(time);
	}
}