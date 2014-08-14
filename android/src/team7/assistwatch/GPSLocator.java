package team7.assistwatch;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

public class GPSLocator implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
	
	// Constants.
	private static final String TAG = GPSLocator.class.getName();

	Context context;
	LocationClient mLocationClient;
	boolean connected;
	boolean connectionFailed;
	
	public GPSLocator(Context context) {
		this.context = context;
        mLocationClient = new LocationClient(context, this, this);
	}
	
    @Override
    public void onConnected(Bundle dataBundle) {
    	connected = true;
    	connectionFailed = false;
    }
    
    @Override
    public void onDisconnected() {
    	connected = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    	connectionFailed = true;
    }
	
	public void connect() {
		mLocationClient.connect();
	}
	
	public void disconnect() {
		mLocationClient.disconnect();
	}
	
	public Location getCurrentLocation() throws GPSLocationUnavailableException {
    	Location loc = mLocationClient.getLastLocation();
    	if (loc == null) {
    		String msg = "Location not available";
    		Log.d(TAG, msg);
        	throw new GPSLocationUnavailableException(msg);
    	}
    	return loc;
	}
	
	public GeographicCoordinates getCoordinates() throws GPSLocationUnavailableException {
		GeographicCoordinates coords = new GeographicCoordinates();
		coords.latitude = getCurrentLocation().getLatitude();
		coords.longitude = getCurrentLocation().getLongitude();
		return coords;
	}
}
