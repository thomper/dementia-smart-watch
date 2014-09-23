package com.team7.smartwatch.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;

import com.team7.smartwatch.shared.Patient;
import com.team7.smartwatch.shared.Utility;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public abstract class PatientReaderTask extends AsyncTask<HttpContext, Void, Void> {
	
	private static final String TAG = PatientReaderTask.class.getName();
	private static final String GET_URL = Globals.get().SERVER_ADDRESS + "/readpatients";
	
	public List<Patient> mPatients;
	
	@Override
	protected Void doInBackground(HttpContext... params) {

		HttpGet request = new HttpGet(GET_URL);

		AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		HttpContext httpContext = params[0];
		try {
			HttpResponse response = client.execute(request, httpContext);
			buildPatientsList(response);
			onPatientsRead();
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
			JSONArray jsonArray = AndroidUtility.JSONArrayFromString(responseStr);
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.length(); ++i) {
					mPatients.add(new Patient(jsonArray.getJSONObject(i)));
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
    
    abstract void onPatientsRead();
}