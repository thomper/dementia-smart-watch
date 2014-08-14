package team7.assistwatch;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MainActivity extends Activity {

	// Constants.
	private static final String TAG = GPSLocator.class.getName();
	
	private GPSLocator locator;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        locator = new GPSLocator(this);
        checkPlayServicesAvailable();
    }
    
    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        locator.connect();
    }
    
    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        locator.disconnect();
        super.onStop();
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        	
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    
    void checkPlayServicesAvailable() {
    	int resultCode =
    			GooglePlayServicesUtil.
    				isGooglePlayServicesAvailable(this);
    	if (resultCode != ConnectionResult.SUCCESS) {
    		String msg = "Google Play Services unavailable" +
    					 " result code: " + String.valueOf(resultCode);
    		Log.d(TAG, msg);
        	Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, -1);
        	errorDialog.show();
        }
    }
    
    public void updateCoordinates(View view) {
    	TextView textViewLatitude = (TextView) findViewById(R.id.text_latitude);
    	TextView textViewLongitude = (TextView) findViewById(R.id.text_longitude);
		
		try {
			GeographicCoordinates coords = locator.getCoordinates();
			textViewLatitude.setText(String.valueOf(coords.latitude));
			textViewLongitude.setText(String.valueOf(coords.longitude));
		} catch (GPSLocationUnavailableException e) {
			textViewLatitude.setText("Unavailable");
			textViewLongitude.setText("Unavailable");
		}
		
		updateConnectionStatus(view);
    }
    
    public void updateConnectionStatus(View view) {
    	TextView textStatus = (TextView) findViewById(R.id.text_status);
    	if (locator.connected) {
    		textStatus.setText("Connected");
    	} else {
    		textStatus.setText("No connection");
    	}
    }
}
