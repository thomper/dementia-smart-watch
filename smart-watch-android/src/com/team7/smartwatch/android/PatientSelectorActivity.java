package com.team7.smartwatch.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;

import com.team7.smartwatch.shared.Patient;
import com.team7.smartwatch.shared.Utility;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class PatientSelectorActivity extends Activity {
	
	private List<Patient> mPatients;

	private static final String TAG = PatientSelectorActivity.class.getName();
	private static final String GET_URL = Globals.get().SERVER_ADDRESS
			+ "/readpatients";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patient_selector);

		// Call Async task to read patients and populate list.
		new PatientReaderTask().execute(Globals.get().httpContext);
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
	
	public class PatientReaderTask extends AsyncTask<HttpContext, Void, Void> {
		
		@Override
		protected Void doInBackground(HttpContext... params) {

			HttpGet request = new HttpGet(GET_URL);
	
			AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
			HttpContext httpContext = params[0];
			try {
				HttpResponse response = client.execute(request, httpContext);
				buildPatientsList(response);
			} catch (IOException e) {
				Log.e(TAG, Utility.StringFromStackTrace(e));
			} finally {
				client.close();
			}
			
			return null;
		}
		
	    private void buildPatientsList(HttpResponse response) {
	        
	    	mPatients = new ArrayList<Patient>();

			try {
				String responseStr = AndroidUtility.StringFromHttpResponse(response);
				
				Log.e(TAG, "REMOVE THIS Patients responseStr: " + responseStr);
				
				String headers = "";
				for (Header header : response.getAllHeaders()) {
					headers += header.getName() + "\n" + header.getValue() + "\n";
				}
				Log.d(TAG, headers);
				
				JSONArray jsonArray = AndroidUtility.JSONArrayFromString(responseStr);
				if (jsonArray != null) {
					for (int i = 0; i < jsonArray.length(); ++i) {
						mPatients.add(new Patient(jsonArray.getJSONObject(i)));
						Log.e(TAG, "REMOVE THIS Next patient is: " + mPatients.get(mPatients.size() - 1).firstName);
					}
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
}