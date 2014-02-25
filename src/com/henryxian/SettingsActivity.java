package com.henryxian;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends SherlockPreferenceActivity{
	private static final String TAG = SettingsActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}
	
}
