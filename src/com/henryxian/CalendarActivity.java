package com.henryxian;

import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

public class CalendarActivity extends SherlockFragmentActivity {
	private static final String TAG = CalendarActivity.class.getSimpleName();
	
	private CaldroidFragment caldroidFragment;
	ActionMode mActionMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);

		caldroidFragment = new CaldroidFragment();
		Button button = (Button)findViewById(R.id.customize_button);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(CalendarActivity.this, ShowRecentEventActivity.class));
			}
		});
		
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
			
			// initialize the caldroid with preference
			int year = DatePickerPreference.getYear(date);
			int month = DatePickerPreference.getMonth(date) - 1;
			int day = DatePickerPreference.getDay(date);
			
			// set the displayed month and year on fresh
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));	
			
			// some other custom settings
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
			args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
			
			caldroidFragment.setArguments(args);
			
			cal.set(year, month, day);
			Date minDate = cal.getTime();
			caldroidFragment.setMinDate(minDate);
			Log.d(TAG, "pref date: " + cal.getTime().toString());
			cal.add(Calendar.DATE, 140);
			Date maxDate = cal.getTime();
			caldroidFragment.setMaxDate(maxDate);
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
			
			@Override
			public void onLongClickDate(Date date, View view) {
				// TODO Auto-generated method stub
//				Toast.makeText(getApplicationContext(), "work", Toast.LENGTH_SHORT)
//					.show();
				registerForContextMenu(view);
				mActionMode = startActionMode(new AnActionModeOfEpicProportions());
				Intent intent = new Intent(CalendarActivity.this, AddEventActivity.class);
				
//				int year = date.getYear();
//				int month = date.getMonth() + 1;
//				int day = date.getDate();
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				int year = cal.get(Calendar.YEAR);
				int month = cal.get(Calendar.MONTH) + 1;
				
				String month2;
				if (month < 10) {
					 month2 = "0" + month;
				} else {
					 month2 = String.valueOf(month);
				}
				
				int day = cal.get(Calendar.DAY_OF_MONTH);
				String day2;
				if (day < 10) {
					day2 = "0" + day;
				} else {
					day2 = String.valueOf(day);
				}
				
				intent.putExtra("Date", year + "-" + month2 + "-" + day2);
				startActivity(intent);
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
		
		// TODO set the item's intent
		Calendar cal = Calendar.getInstance();
		
		menu.add(Menu.NONE, 1, Menu.NONE, R.string.menu_add_event)
		.setIntent(new Intent(this, AddEventActivity.class))
		.setIcon(R.drawable.ic_action_new)
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
		menu.add(Menu.NONE, 2, Menu.NONE, R.string.menu_settings)
			.setIntent(new Intent(this, SettingsActivity.class))
			.setIcon(R.drawable.ic_action_settings)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		
		return true;
	}
	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//		//Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show();
//		switch (item.getItemId()) {
//		case 2:
//			startActivity(new Intent(this, SettingsActivity.class));
//		}
//		return true;
//	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		menu.add("test");
	}
	
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Got clicked!: " + item.toString(), Toast.LENGTH_SHORT).show();
		
		return true;
	}

	// TODO try to use action mode 
	private final class AnActionModeOfEpicProportions implements ActionMode.Callback {

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			menu.add("edit")
				.setIcon(R.drawable.ic_action_edit)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
			
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			// TODO Auto-generated method stub
			Toast.makeText(CalendarActivity.this, "Got clicked:" + item, Toast.LENGTH_SHORT).show();
			mode.finish();
			
			return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			// TODO Auto-generated method stub
			
		}
	}
}
