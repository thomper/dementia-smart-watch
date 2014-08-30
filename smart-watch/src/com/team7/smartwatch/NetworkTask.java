package com.team7.smartwatch;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class NetworkTask extends AsyncTask<HttpUriRequest, Void, HttpResponse> {
	
	private static final String TAG = NetworkTask.class.getName();
    
	@Override
	protected HttpResponse doInBackground(HttpUriRequest... params) {
		HttpUriRequest request = params[0];
		AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		try {
			return client.execute(request);
		} catch (IOException e) {
			Log.e(TAG, e.getLocalizedMessage());
			return null;
		} finally {
			client.close();
		}
	}

    @Override
    protected void onPostExecute(HttpResponse result) {
        
        if (result != null) {
        	// TODO: Check the response, log if there are errors.
        }
    }
}
