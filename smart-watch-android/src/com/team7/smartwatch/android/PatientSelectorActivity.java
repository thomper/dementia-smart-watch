package com.team7.smartwatch.android;


import com.team7.smartwatch.shared.Patient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PatientSelectorActivity extends Activity {
	
	private ReaderTask readerTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_selector);

		((ListView) findViewById(R.id.listview)).setOnItemClickListener(
				mPatientClickedHandler);

		// Call Async task to read patients and populate list.
		readerTask = new ReaderTask();
		readerTask.execute(Globals.get().httpContext);
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

	// Create a message handling object as an anonymous class.
	private OnItemClickListener mPatientClickedHandler = new OnItemClickListener() {

	    public void onItemClick(AdapterView parent, View v, int position, long id) {

	    	Patient patient = readerTask.mPatients.get(position);
	        showConfirmationDialog(patient);
	    }
	};
	
	public class ReaderTask extends PatientReaderTask {

		@Override
		void onPatientsRead() {
			
			String[] patientsArray = summaryStringsFromPatientList();
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					PatientSelectorActivity.this,
			        android.R.layout.simple_list_item_1, patientsArray);
			((ListView) findViewById(R.id.listview)).setAdapter(adapter);
		}
		
		/** Create an array of short summary strings from a list of patients. */
		private String[] summaryStringsFromPatientList() {
			
			String[] patientsArray = new String[mPatients.size()];
			for (int i = 0; i < mPatients.size(); ++i) {
				Patient patient = mPatients.get(i);
				patientsArray[i] = summaryStringFromPatient(patient);
			}
			
			return patientsArray;
		}
		
		private String summaryStringFromPatient(Patient patient) {
			
				return patient.firstName + " " +
						patient.lastName + ",\n" +
						patient.homeAddress + "\n" +
						patient.homeSuburb;
		}
	}
	
	private void showConfirmationDialog(final Patient patient) {
		
		String patientName = patient.firstName + " " + patient.lastName;
	    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setMessage("This device belongs to " + patientName + ".")
	           .setCancelable(true)
	           .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, final int id) {
	                   Intent intent = new Intent(PatientSelectorActivity.this,
	                		   MainActivity.class);
	                   String patientJSON = patient.toJSON().toString();
	                   intent.putExtra("patient", patientJSON);
	                   startActivity(intent);
	               }
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	}
}