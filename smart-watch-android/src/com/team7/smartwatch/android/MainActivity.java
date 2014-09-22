package com.team7.smartwatch.android;

import com.team7.smartwatch.shared.Patient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@SuppressWarnings("unused")
	private Locator mLocator;	
	private Patient mPatient;
	private MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.error);
	final int NOTIF_ID = 4260;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// mPatient will be passed to MainActivity by LoginActivity
		mPatient = new Patient();
		mPatient.patientID = 4;
		lockOrientationToPortrait();
		setupPanicButton();
		setupDetailsButton();
		startTrackingLocation();
		setupConnectionTracking();
	}

	private void setupConnectionTracking() {
		Handler h = new Handler();
		int delay = 30000; //milliseconds
		
		h.postDelayed(new Runnable(){
		    public void run(){
		    	NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    	Notification note = new Notification(R.drawable.wififail, "No Connection", System.currentTimeMillis());
		    	PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
		    			new Intent(getApplicationContext(), MainActivity.class), 0);
		    	
		        if(!connectedToInternet()){
		        	mp.start();
		        	 note.setLatestEventInfo(getApplicationContext(), "No Connection", 
		        			 "You need to connect to the internet.", intent);
		        	 
		        	 notifManager.notify(NOTIF_ID, note);
		        }else{
		        	notifManager.cancel(NOTIF_ID);
		        }
		    }
		}, delay);
		
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

		Button panicButton = (Button)findViewById(R.id.panic_button);
		panicButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
					sendText();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+retrieveNumber()));
                    startActivity(callIntent);
			}
		});
	}
	
	private void setupDetailsButton() {

		storeData();
		Button detailsButton = (Button) findViewById(R.id.details_button);
		detailsButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(MainActivity.this,
						PatientDetailsActivity.class);
				startActivity(intent);
			}
		});
	}
	
	//returns whether the phone is connected to the internet or not
	private boolean connectedToInternet(){
		ConnectivityManager connectivityManager 
        = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
	
	//The method that's used to store the patient's data on the device
	public void storeData() {

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
		mLocator = new Locator(this, mPatient.patientID);
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
