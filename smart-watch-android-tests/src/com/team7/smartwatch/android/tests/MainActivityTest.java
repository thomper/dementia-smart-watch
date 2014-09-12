package com.team7.smartwatch.android.tests;

import android.test.ActivityInstrumentationTestCase2;

import com.team7.smartwatch.android.MainActivity;

public class MainActivityTest
		extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private MainActivity mainActivity;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mainActivity = getActivity();
	}
	
	public void testPreconditions() {
	    assertNotNull("mainActivity is null", mainActivity);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
