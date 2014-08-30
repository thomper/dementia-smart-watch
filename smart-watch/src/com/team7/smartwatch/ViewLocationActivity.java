package com.team7.smartwatch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

public class ViewLocationActivity extends Activity {

	Locator locator;

	private static final String TAG = ViewLocationActivity.class.getName();

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
		locator = new Locator(this);
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
}