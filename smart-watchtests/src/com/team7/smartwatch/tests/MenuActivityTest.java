package com.team7.smartwatch.tests;

import android.test.ActivityInstrumentationTestCase2;

import com.team7.smartwatch.MenuActivity;

public class MenuActivityTest
		extends ActivityInstrumentationTestCase2<MenuActivity> {
	
	private MenuActivity menuActivity;

	public MenuActivityTest() {
		super(MenuActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		menuActivity = getActivity();
	}
	
	public void testPreconditions() {
	    assertNotNull("menuActivity is null", menuActivity);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
