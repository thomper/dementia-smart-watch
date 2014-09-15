package com.team7.smartwatch.android;

import com.team7.smartwatch.shared.Patient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private Locator locator;	
	private Patient patient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// patient will be passed to MainActivity by LoginActivity
		patient = new Patient();
		patient.ID = 4;
		lockOrientationToPortrait();
		setupPanicButton();
		startTrackingLocation();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		return super.onOptionsItemSelected(item);
	}
	
	private void lockOrientationToPortrait() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	private void setupPanicButton() {
		Button panicButton = (Button)findViewById(R.id.detailsButton);
		storeData();
		panicButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
					sendText();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+retrieveNumber()));
                    startActivity(callIntent);
			}
		});
	}
	
	//The method that's used to store the users data on the device
	public void storeData(){
		SharedPreferences patientDetails = getApplicationContext().getSharedPreferences("PatientDetails", 0);
		SharedPreferences.Editor editor = patientDetails.edit();
		editor.putString("fName", "aaron");
		editor.putString("lName", "ramsey");
		editor.putString("gender", "Male");
		editor.putString("age", "88");
		editor.putString("bloodType", "A");
		editor.putString("medication", "");
		editor.putString("homeAddress", "322 Moggill Road");
		editor.putString("homeSuburb", "Indooroopilly");
		editor.putString("contactNum", "8877665544");
		editor.putString("emergencyContactName", "louise elliot");
		editor.putString("emergencyContactSuburb", "");
		editor.putString("emergencyContactAddress", "");
		editor.putString("emergencyContactNumber", "0423787149");

		
		//Apply Data
		editor.apply();

		
	}
	
	//The method that's used to store the users data on the device
	//NOTE:Doesn't store carerID, patientID, and status
	public void storeData(String[] data){
		SharedPreferences patientDetails = getApplicationContext().getSharedPreferences("PatientDetails", 0);
		SharedPreferences.Editor editor = patientDetails.edit();
		editor.putString("fName", data[0]);
		editor.putString("lName", data[1]);
		editor.putString("gender", data[2]);
		editor.putString("age", data[3]);
		editor.putString("bloodType", data[4]);
		editor.putString("medication", data[5]);
		editor.putString("homeAddress", data[6]);
		editor.putString("homeSuburb", data[7]);
		editor.putString("contactNum", data[8]);
		editor.putString("emergencyContactName", data[9]);
		editor.putString("emergencyContactSuburb", data[10]);
		editor.putString("emergencyContactAddress", data[11]);
		editor.putString("emergencyContactNumber", data[12]);

			
		//Apply Data
		editor.apply();

			
	}
	
	@SuppressLint("UnlocalizedSms") public void sendText(){
		SmsManager.getDefault().sendTextMessage(retrieveNumber(), null, "Your Patient is paniced!", null, null);
	}
	
	private String retrieveNumber(){
		SharedPreferences patientDetails = getApplicationContext().getSharedPreferences("PatientDetails", 0);
		String number = patientDetails.getString("emergencyContactNumber", "");
		return number;
	}
	
	private void startTrackingLocation() {
		if (!gpsEnabled()) {
			showDialogNoGps();
		}
		locator = new Locator(this, patient.ID);
	}
	
	private boolean gpsEnabled() {
		final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
	    return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

    private void showDialogNoGps() {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("GPS must be enabled for location tracking")
	           .setCancelable(false)
	           .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, final int id) {
	                   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	}
}
