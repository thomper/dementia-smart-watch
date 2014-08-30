package com.team7.smartwatch;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Locator {

	private Context context;
	private LocationManager locationManager;
	private LocationListener locationListener;
	
	private static final String TAG = Locator.class.getName();
	private static final String POST_URL = "http://192.168.1.16:8080/updatelocation";

	public Locator(Context context) {
		this.context = context;
		locationListener = new MyLocationListener();
		locationManager = (LocationManager) this.context
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				5000, 10, locationListener);
	}
	
	private class MyLocationListener implements LocationListener {

		// This function for testing only, REMOVE!
		private void showLocation(Location loc) {
			Toast.makeText(
					context,
					"Location changed - Lat: " + loc.getLatitude() + " Lng: "
							+ loc.getLongitude(), Toast.LENGTH_SHORT).show();
		}

		private void logLocation(Location loc) {
			String latitude = "Latitude: " + String.valueOf(loc.getLatitude());
			Log.v(TAG, latitude);
			String longitude = "Longitude: "
					+ String.valueOf(loc.getLongitude());
			Log.v(TAG, longitude);
		}

		private void postLocation(Location loc) {
			
			// Create the post request.
			HttpPost request = new HttpPost(POST_URL);
			String patientID = "4";  // TODO: TESTING ONLY!
			String latit = String.valueOf(loc.getLatitude());
			String longit = String.valueOf(loc.getLongitude());
			List<NameValuePair> pairs =
					new ArrayList<NameValuePair>(3);
			pairs.add(new BasicNameValuePair("patientID", patientID));
			pairs.add(new BasicNameValuePair("latitude", latit));
			pairs.add(new BasicNameValuePair("longitude", longit));
			try {
				request.setEntity(new UrlEncodedFormEntity(pairs));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Execute the request asynchronously.
			NetworkTask task = new NetworkTask();
			task.execute(request);
		}

		@Override
		public void onLocationChanged(Location loc) {
			showLocation(loc); // Testing only, REMOVE
			logLocation(loc);
			postLocation(loc);
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
}
