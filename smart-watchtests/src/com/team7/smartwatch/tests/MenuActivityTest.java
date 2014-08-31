package com.team7.smartwatch.tests;

import com.team7.smartwatch.MenuActivity;
import com.team7.smartwatch.R;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

public class MenuActivityTest
		extends ActivityInstrumentationTestCase2<MenuActivity> {
	
	private MenuActivity menuActivity;
	private Button btnLocation;

	public MenuActivityTest() {
		super(MenuActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		menuActivity = getActivity();
		btnLocation = (Button) menuActivity
					  .findViewById(R.id.btnLocation);
	}
	
	public void testPreconditions() {
	    assertNotNull("menuActivity is null", menuActivity);
	    assertNotNull("btnLocation is null", btnLocation);
	}
	
	public void testbtnLocation_labelText() {
	    final String expected =
	            menuActivity.getString(R.string.button_location);
	    final String actual = btnLocation.getText().toString();
	    assertEquals(expected, actual);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
}
