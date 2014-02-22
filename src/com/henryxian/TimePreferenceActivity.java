package com.henryxian;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class TimePreferenceActivity extends SherlockPreferenceActivity{
	private TimePickerPreference timePreference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
	
}
