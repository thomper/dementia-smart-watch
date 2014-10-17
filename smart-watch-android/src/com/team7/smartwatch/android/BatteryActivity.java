package com.team7.smartwatch.android;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class BatteryActivity extends Activity {

	TextView mTextView;
	float mBatteryLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_battery);
		mTextView = (TextView) findViewById(R.id.battery_status);
		findViewById(R.id.battery_getbutton).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						mTextView.setText(String.valueOf(mBatteryLevel));
					}
				});
		new Thread(new ThreadM()).start();
	}

	private class ThreadM implements Runnable {

		@Override
		public void run() {
			mBatteryLevel = getBatteryLevel();
			Log.e("Battery Level", String.valueOf(mBatteryLevel));
		}
	}

	public float getBatteryLevel() {

		Intent batteryIntent = registerReceiver(null, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
		if (level == -1 || scale == -1) {
			return -1;
		}
		return ((float) level / (float) scale) * 100.0f;
	}
}