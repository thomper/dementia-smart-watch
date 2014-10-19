package com.team7.smartwatch.shared;

public class RadialFence {
	
	double mLatitude;   // degrees
	double mLongitude;  // degrees
	double mRadius;     // metres
	
	public RadialFence(double latitude, double longitude, double radius) {
		
		mLatitude = latitude;
		mLongitude = longitude;
		mRadius = radius;
	}

    public boolean containsLocation(double latitude, double longitude) {

		double METRES_PER_DEGREE =  Math.PI * 6360 * 1000 / 180;

		Double dew = latitude - mLatitude;
		Double dns = longitude - mLongitude;
		Double distFromFence  = Math.sqrt( dew * dew + dns * dns );
		distFromFence = distFromFence * METRES_PER_DEGREE;
		
		return distFromFence < mRadius;
    }
}