package com.team7.smartwatch.android;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class PatientDetailsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		
		displayStoredData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.patient_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void displayStoredData() {
		
		SharedPreferences patientDetails = getApplicationContext()
				.getSharedPreferences("PatientDetails", 0);

		TextView tv = (TextView)findViewById(R.id.first_name);
		tv.setText(patientDetails.getString("fName", ""));

		tv = (TextView)findViewById(R.id.last_name);
		tv.setText(patientDetails.getString("lName", ""));

		tv = (TextView)findViewById(R.id.blood_type);
		tv.setText(patientDetails.getString("bloodType", ""));

		tv = (TextView)findViewById(R.id.medication);
		tv.setText(patientDetails.getString("medication", ""));

		tv = (TextView)findViewById(R.id.residence);
		String residence = patientDetails.getString("homeAddress", "") + "\n" +
				patientDetails.getString("homeSuburb", "") + "\n" +
				patientDetails.getString("contactNum", "");
		tv.setText(residence);

		tv = (TextView)findViewById(R.id.emergency_contact);
		String emergencyContact = patientDetails.getString("emergencyContactName", "") + "\n" +
				patientDetails.getString("emergencyContactAddress", "") + "\n" +
				patientDetails.getString("emergencyContactSuburb", "") + "\n" +
				patientDetails.getString("emergencyContactNumber", "");
		tv.setText(emergencyContact);
	}
}
