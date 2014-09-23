package com.team7.smartwatch.android;

import com.team7.smartwatch.shared.Patient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class PatientSelectorActivity extends Activity {
	
	private static final String TAG = PatientSelectorActivity.class.getName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_selector);

		// Call Async task to read patients and populate list.
		new ReaderTask().execute(Globals.get().httpContext);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.patient_selector, menu);
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
	
	public class ReaderTask extends PatientReaderTask {

		@Override
		void onPatientsRead() {
			
			for (Patient patient: mPatients) {
				Log.e("TEST", patient.firstName + " " + patient.lastName);
			}
		}
	}
}