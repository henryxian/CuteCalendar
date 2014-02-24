package com.henryxian;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnSharedPreferenceChangeListener {
	// private final static String 
	private static final String TAG = "MainActivity";
	public static final String PREFS_TIME = "TimePreference";
	public static final String PREFS_DATE = "DatePreference";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		final Button button = (Button)findViewById(R.id.button_to_settings);
		final TextView textView = (TextView)findViewById(R.id.text_display);
		final TextView textView2 = (TextView)findViewById(R.id.text_display2);
				
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this, TimePreferenceActivity.class));
			}
		});
		
		SharedPreferences settings = 
				PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		settings.registerOnSharedPreferenceChangeListener(this);
		
		String mTime = settings.getString(PREFS_TIME, "00:00");
		String mDate = settings.getString("DatePreference", "2013-2-22");
		
		textView.setText(mTime);
		textView2.setText(mDate);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		Log.i(TAG, "preference listener works!");
		Log.i(TAG, key + " changed!");
		
		if (key == PREFS_DATE) {
			Log.i(key, "Date preference changed!");
			String sp = sharedPreferences.getString(key, "2013-2-22");
			TextView textView = (TextView) findViewById(R.id.text_display);
			textView.setText(sp);
			Toast.makeText(this, "change!", Toast.LENGTH_LONG).show();
		}
		
		if (key == PREFS_TIME) {
			Log.i(key,  "Time preference changed!");
			String sp = sharedPreferences.getString(key, "00:00");
			TextView textView = (TextView) findViewById(R.id.text_display2);
			textView.setText(sp);
		}
	}

}
