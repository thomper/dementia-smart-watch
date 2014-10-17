package com.team7.smartwatch.android;

import org.json.JSONException;
import org.json.JSONObject;

import com.team7.smartwatch.shared.Patient;

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
		
        JSONObject patientJSON = null;
		try {
			patientJSON = new JSONObject(getIntent().getExtras().getString("patient"));
		} catch (JSONException e) {
			// TODO Communicate error and either go back or exit
			e.printStackTrace();
		}
        Patient patient = new Patient(patientJSON);

		TextView tv = (TextView)findViewById(R.id.first_name);
		tv.setText(patient.firstName);

		tv = (TextView)findViewById(R.id.last_name);
		tv.setText(patient.lastName);
		
		tv = (TextView)findViewById(R.id.phone);
		tv.setText(patient.contactNum);

		tv = (TextView)findViewById(R.id.blood_type);
		tv.setText(patient.bloodType.toString().toLowerCase());

		tv = (TextView)findViewById(R.id.medication);
		tv.setText(patient.medication);

		tv = (TextView)findViewById(R.id.residence);
		String residence = patient.homeAddress + "\n" +
				patient.homeSuburb + "\n" +
				patient.contactNum;
		tv.setText(residence);

		tv = (TextView)findViewById(R.id.emergency_contact);
		String emergencyContact = patient.emergencyContact.name + "\n" +
				patient.emergencyContact.address + "\n" +
				patient.emergencyContact.suburb;
		tv.setText(emergencyContact);
		
		tv = (TextView)findViewById(R.id.emergency_number);
		tv.setText(patient.emergencyContact.num);
	}
}
