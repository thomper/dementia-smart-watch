package com.team7.smartwatch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class GetCurrentLocation extends Activity {

	private LocationManager locationManager = null;
	private LocationListener locationListener = null;

	private static final String POST_URL = "http://172.19.4.106:8080/mypage.php";
	private static final String TAG = GetCurrentLocation.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Lock screen to portrait mode.
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		// Setup location tracking.
		if (!displayGpsStatus()) {
			alertbox("Gps Disabled", "GPS location tracking is currently disabled.");
		}
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new MyLocationListener();
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
	}

	/*----Method to Check GPS is enable or disable ----- */
	private Boolean displayGpsStatus() {
		ContentResolver contentResolver = getBaseContext().getContentResolver();
		@SuppressWarnings("deprecation")
		boolean gpsStatus = Settings.Secure.isLocationProviderEnabled(
				contentResolver, LocationManager.GPS_PROVIDER);
		if (gpsStatus) {
			return true;

		} else {
			return false;
		}
	}

	/*----------Method to create an AlertBox ------------- */
	protected void alertbox(String title, String mymessage) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Your Device's GPS is Disable")
				.setCancelable(false)
				.setTitle("** Gps Status **")
				.setPositiveButton("Gps On",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// finish the current activity
								// AlertBoxAdvance.this.finish();
								Intent myIntent = new Intent(
										Settings.ACTION_SECURITY_SETTINGS);
								startActivity(myIntent);
								dialog.cancel();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// cancel the dialog box
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/*----------Listener class to get coordinates ------------- */
	private class MyLocationListener implements LocationListener {
		
		// This function for testing only, REMOVE!
		private void showLocation(Location loc) {
			Toast.makeText(
					getBaseContext(),
					"Location changed : Lat: " + loc.getLatitude() + " Lng: "
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
			
			// Create a new HttpClient and Post Header
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(POST_URL);

			String longit = String.valueOf(loc.getLongitude());
			String latit = String.valueOf(loc.getLatitude());
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				nameValuePairs
						.add(new BasicNameValuePair("latitude", longit));
				nameValuePairs
						.add(new BasicNameValuePair("longitude", latit));
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httpClient.execute(httpPost);

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
		
		@Override
		public void onLocationChanged(Location loc) {
			showLocation(loc);  // Testing only, REMOVE
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