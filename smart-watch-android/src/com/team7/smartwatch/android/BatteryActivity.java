package com.team7.smartwatch.android;

 
import android.app.Activity;  
import android.content.BroadcastReceiver;  
import android.content.Context;  
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
      BatteryReceiver mArrow;  

      @Override  
      protected void onCreate(Bundle savedInstanceState) {  
           super.onCreate(savedInstanceState);  
           setContentView(R.layout.activity_battery);  
           mTextView = (TextView) findViewById(R.id.battery_status);  
           findViewById(R.id.battery_getbutton).setOnClickListener(new OnClickListener() {  
                @Override  
                public void onClick(View v) {  
                     mTextView.setText(String.valueOf(mBatteryLevel));  
                }  
           });  
           new Thread(new ThreadM()).start();  
      }  

      @Override  
      protected void onDestroy() {  
           super.onDestroy();  
           unregisterReceiver(mArrow);  
      }  

      private class ThreadM implements Runnable {  

           @Override  
           public void run() {  
                mArrow = new BatteryReceiver();  
                IntentFilter intentFilter = new IntentFilter();  
                intentFilter.addAction(Intent.ACTION_BATTERY_LOW);  
                intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);  
                intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);  
                Intent batteryIntent = registerReceiver(mArrow, intentFilter);  
                mBatteryLevel = getBatteryLevel(batteryIntent);  
                Log.e("Battery Level", String.valueOf(mBatteryLevel));  
           }  
      }  
      
      
      private class BatteryReceiver extends BroadcastReceiver {  
          @Override  
          public void onReceive(Context arg0, Intent arg1) {  
               
          }  
     }   
      
      public float getBatteryLevel(Intent batteryIntent) {  
           int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);  
           int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);  
           if (level == -1 || scale == -1) {  
                return 50.0f;  
           }  
           return ((float) level / (float) scale) * 100.0f;  
      }  
      

 }  
 