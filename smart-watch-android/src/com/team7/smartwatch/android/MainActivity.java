package com.team7.smartwatch.android;

import org.json.JSONException;
import org.json.JSONObject;

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
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
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
	private MediaPlayer mp;
	
	// Collapse detection variables.
	private final int NOTIF_ID = 4260;
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private boolean fallInitiate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Attempt to read patient information.
		try {
			JSONObject patientJSON = new JSONObject(getIntent().getExtras().getString("patient"));
			mPatient = new Patient(patientJSON);
		} catch (JSONException e) {
			// TODO Show error and quit or go back to login screen
			e.printStackTrace();
		}
		
		lockOrientationToPortrait();
		mp = MediaPlayer.create(getApplicationContext(), R.raw.error);
		setupPanicButton();
		setupDetailsButton();
		startTrackingLocation();
		startTrackingBatteryCharge();
		setupConnectionTracking();
		initialiseFallDetection();
	}
	
	//
	public void onSensorChanged(SensorEvent event){
		//xyz acceleration rate variables are created
		  float x = event.values[0];
		  float y = event.values[1];
		  float z = event.values[2];
		  
		  //makes all values positive before adding together and square root them
		  double sigma = x*x + y*y + z*z;
		  sigma= java.lang.Math.sqrt(sigma);
		  
		  if(fallInitiate){
			  if(sigma>=13){
				  //PATIENT HAS FALLEN
				  NotificationManager notifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			    	Notification note = new Notification(R.drawable.wififail, "No Connection", System.currentTimeMillis());
			    	PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0,
			    			new Intent(getApplicationContext(), MainActivity.class), 0);
			    	note.setLatestEventInfo(getApplicationContext(), "No Connection", 
		        			 "You need to connect to the internet.", intent);
			    	
			  }else{
				  fallInitiate=false;
			  }
		  }
		  
		  if(sigma<=7){
			  fallInitiate=true;
		  }
		  
		  
		}

	private void initialiseFallDetection() {
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// TODO: We need to call mSensorManager.registerListener at some point.
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
					new Alerter(getApplicationContext(), mPatient.patientID);
					sendText();
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+retrieveNumber()));
                    startActivity(callIntent);
			}
		});
	}
	
	private void setupDetailsButton() {

		Button detailsButton = (Button) findViewById(R.id.details_button);
		detailsButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View v){
				Intent intent = new Intent(MainActivity.this,
						PatientDetailsActivity.class);
				intent.putExtra("patient", mPatient.toJSON().toString());
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
	
	@SuppressLint("UnlocalizedSms") public void sendText(){

		SmsManager.getDefault().sendTextMessage(retrieveNumber(), null, "Your Patient is panicked!", null, null);
	}
	
	private String retrieveNumber(){

		return mPatient.emergencyContact.num;
	}
	
	private void startTrackingLocation() {

		if (!gpsEnabled()) {
			showDialogNoGps();
		}
		mLocator = new Locator(this, mPatient);
	}
	
	private void startTrackingBatteryCharge() {
		
		final Handler handler = new Handler();

		handler.post(new Runnable() {
		    public void run(){
		    	new BatteryUpdater(MainActivity.this, mPatient.patientID,
		    			retrieveNumber());
			   	handler.postDelayed(this, Globals.get().BATTERY_READING_INTERVAL);
		    }
		});
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
