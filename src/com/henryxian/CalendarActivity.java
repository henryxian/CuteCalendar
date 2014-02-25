package com.henryxian;

import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

@SuppressLint("SimpleDateFormat")
public class CalendarActivity extends SherlockFragmentActivity {
	private static final String TAG = CalendarActivity.class.getSimpleName();
	
	private CaldroidFragment caldroidFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);

		caldroidFragment = new CaldroidFragment();
		
		if (savedInstanceState != null) {
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		}
		// If activity is created from fresh
		else {
			// TODO initialize the calendar with preference
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
			String date = sp.getString("DatePreference", "2013-2-23");
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			
			int year = DatePickerPreference.getYear(date);
			int month = DatePickerPreference.getMonth(date) - 1;
			int day = DatePickerPreference.getDay(date);
			
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));	
			
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

			cal.set(year, month, day);
			caldroidFragment.setArguments(args);
			caldroidFragment.setMinDate(cal.getTime());
			Log.d(TAG, "pref date: " + cal.getTime().toString());
			cal.add(Calendar.DATE, 140);
			caldroidFragment.setMaxDate(cal.getTime());
		}

		// Attach to the activity
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.fragment_calendar, caldroidFragment);
		t.commit();

		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				Toast.makeText(getApplicationContext(), "work",
						Toast.LENGTH_SHORT).show();
			}

		};

		// Setup Caldroid
		caldroidFragment.setCaldroidListener(listener);
	}
	
	/**
	 * Save current states of the Caldroid here
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		if (caldroidFragment != null) {
			caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
		}
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
//		return super.onCreateOptionsMenu(menu);
		menu.add(R.string.menu_settings)
			.setIcon(R.drawable.ic_action_settings)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show();
		
		startActivity(new Intent(this, SettingsActivity.class));
		return true;
	}

}
