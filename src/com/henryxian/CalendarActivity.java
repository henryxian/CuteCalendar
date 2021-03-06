package com.henryxian;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
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
import com.henryxian.GCalendarContract.Instances;
import com.roomorama.caldroid.CaldroidListener;

public class CalendarActivity extends SherlockFragmentActivity {
	private static final String TAG = CalendarActivity.class.getSimpleName();
	
	private SchoolWeekCalFragment schoolWeekCalFragment;
	private boolean firsttimeFlag;
	private ActionMode mActionMode;
	private SharedPreferences sp;
	
	private void setDateColor(Date date, int count) {
		if (count == 1) {
			schoolWeekCalFragment.setBackgroundResourceForDate(R.color.free, date);
		}
		
		if (count == 2) {
			schoolWeekCalFragment.setBackgroundResourceForDate(R.color.not_very_busy, date);
		}
		
		if (count == 3) {
			schoolWeekCalFragment.setBackgroundResourceForDate(R.color.medium_busy, date);
		}
		
		if (count > 3) {
			schoolWeekCalFragment.setBackgroundResourceForDate(R.color.busy, date);
		}
	}
	
	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
			// TODO Auto-generated constructor stub
		}
		
		/*
		 * Async fetch the date and event count of the date from 
		 * the database
		 */
		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			// TODO Auto-generated method stub
			super.onQueryComplete(token, cookie, cursor);
			
//			Log.i(TAG, "Cursor_date: " + cursor.getString(0));
//			Log.i(TAG, "Cursor_count: " + cursor.getInt(1));
			if (cursor != null) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				while(cursor.moveToNext()) {
					String date = cursor.getString(0);
					int count = cursor.getInt(1);
					Log.i(TAG, "Cursor_date: " + date);
					Log.i(TAG, "Cursor_count: " + count);
					Date dateTransformed = new Date();
					try {
						dateTransformed = format.parse(date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					SchoolWeekCalFragment.setBackgroundResourceForDate(R.color.caldroid_sky_blue, date);
					setDateColor(dateTransformed, count);
				}
				cursor.close();
			}
		}
	}
	
	/*
	 * initialize the whole calendar fragment
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		schoolWeekCalFragment = new SchoolWeekCalFragment();
		
//		Uri uri = Uri.parse("content://" + EventProvider.AUTHORITY + "/events/counts");		
//		MyAsyncQueryHandler handler = new MyAsyncQueryHandler(this.getContentResolver());
//		handler.startQuery(0, null, uri, null, null, null, null);
		
		setContentView(R.layout.activity_calendar);

		// TODO
		Button button = (Button)findViewById(R.id.customize_button);
		Button button2 = (Button)findViewById(R.id.show_dialog_button);
		sp = PreferenceManager.getDefaultSharedPreferences(this);
		
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				startActivity(new Intent(CalendarActivity.this, ShowRecentEventActivity.class));
				startActivity(new Intent(CalendarActivity.this, ShowAllEventsActivity.class));
			}
		});
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(CalendarActivity.this, AddClassActivity.class));
			}
		});
		
		// restore the fragment
		if (savedInstanceState != null) {
			schoolWeekCalFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		}
		// If activity is created from fresh
		else {
			// TODO initialize the calendar with preference
			String date = sp.getString("DatePreference", "2013-2-23");
			firsttimeFlag = sp.getBoolean("flag", true);
			Bundle args = new Bundle();
			Calendar cal = Calendar.getInstance();
			cal.setFirstDayOfWeek(Calendar.MONDAY);
			
			// initialize the caldroid with preference
			int year = DatePickerPreference.getYear(date);
			int month = DatePickerPreference.getMonth(date) - 1;
			int day = DatePickerPreference.getDay(date);
			
			// set the displayed month and year on fresh
			args.putInt(SchoolWeekCalFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(SchoolWeekCalFragment.YEAR, cal.get(Calendar.YEAR));
			args.putInt(SchoolWeekCalFragment.START_DAY_OF_WEEK, SchoolWeekCalFragment.MONDAY);
			
			// some other custom settings
			args.putBoolean(SchoolWeekCalFragment.ENABLE_SWIPE, true);
			args.putBoolean(SchoolWeekCalFragment.SIX_WEEKS_IN_CALENDAR, true);
			args.putBoolean(SchoolWeekCalFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
			
			schoolWeekCalFragment.setArguments(args);
			
			// Set the start date of week retrieved from
			// the shared preference
			cal.set(year, month, day);
			
			// week starts from Monday
			Log.d(TAG, "day of week:" + 
					String.valueOf(cal.get(Calendar.DAY_OF_WEEK)));
			int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			// calculate the day of week
//			dayOfWeek = dayOfWeek == 0 ? 7 : dayOfWeek;
			
//			dayOfWeek = SchoolCalHelper.dayOfWeek(dayOfWeek);
			// shift the day to the first day of the week.
			cal.add(Calendar.DATE, - SchoolCalHelper.offset(dayOfWeek));
		
			Date minDate = cal.getTime();
			schoolWeekCalFragment.setMinDate(minDate);
			Log.d(TAG, "pref date: " + cal.getTime().toString());
			
			// set the availabe span of the calendar 
			// 20 weeks X 7 days = 140 days
			cal.add(Calendar.DATE, 139);
			Date maxDate = cal.getTime();
			schoolWeekCalFragment.setMaxDate(maxDate);
		}

		// Attach to the activity
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.fragment_calendar, schoolWeekCalFragment);
		t.commit();

		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onSelectDate(Date date, View view) {
//				Toast.makeText(getApplicationContext(), "work",
//						Toast.LENGTH_SHORT).show();
				// TODO
				Intent intent = new Intent(CalendarActivity.this, ShowDayEventActivity.class);
				// getYear() returns the years since 1900
				intent.putExtra("year", date.getYear() + 1900);
				intent.putExtra("month", date.getMonth() + 1);
				Log.d(TAG, "The passing month: " + date.getMonth());
				intent.putExtra("day", date.getDate());
				
				startActivity(intent);
			}
			
			/*
			 * When the month changes, load the instances from the calendar
			 * of the month switched to and refresh the view.
			 */
			@Override
			public void onChangeMonth(int month, int year) {
//				Toast.makeText(CalendarActivity.this, " " + month + " " + year, Toast.LENGTH_SHORT).show();
				Calendar beginTime = Calendar.getInstance();
				beginTime.set(year, month - 1 , 1, 0, 0);
				long startMillis = beginTime.getTimeInMillis();
				int maxDay = beginTime.getActualMaximum(Calendar.DAY_OF_MONTH);
				Log.d(TAG, beginTime.getTime().toString());
				
				Calendar endTime = Calendar.getInstance();
				endTime.set(year, month - 1, maxDay, 23, 59);
				long endMillis = endTime.getTimeInMillis();
				Log.d(TAG, endTime.getTime().toString());
				
				Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
				ContentUris.appendId(builder, startMillis);
				ContentUris.appendId(builder, endMillis);
				
				String[] projection = {
						Instances.EVENT_ID,
						Instances.BEGIN,
						};
				ContentResolver cr = getContentResolver();
				Cursor cursor = cr.query(
							builder.build(), 
							projection, 
							null, 
							null, 
							null
						);
				
				HashMap<String, Integer> count = new HashMap<String, Integer>();
				
				while (cursor.moveToNext()) {
					Calendar begin = Calendar.getInstance();
					begin.setTimeInMillis(cursor.getLong(1));
					String date = begin.get(
							Calendar.YEAR) + "-" + 
							(begin.get(Calendar.MONTH) + 1) + "-" + 
							begin.get(Calendar.DAY_OF_MONTH
						);
					Integer value = count.get(date);
					if (value == null){
						value = 1;
					} else {
						value++;
					}
					count.put(date, value);
					
//					SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//					Log.d(TAG, "cursor: " + cursor.getLong(0)
//							+ " " + formatter.format(begin.getTime())
//							+ " " + cursor.getString(2));
				}
				cursor.close();
				HashMap<String, Object> extraData = new HashMap<String, Object>();
				extraData.put("count", count);
				CalendarActivity.this.schoolWeekCalFragment.setExtraData(extraData);
				
				CalendarActivity.this.schoolWeekCalFragment.refreshView();
			}
			
			/*
			 * When long click the date cell, send the selected date
			 * to the add-event activity and redirect.
			 */
			@Override
			public void onLongClickDate(Date date, View view) {
				registerForContextMenu(view);
//				mActionMode = startActionMode(new AnActionModeOfEpicProportions());
				Intent intent = new Intent(CalendarActivity.this, AddEventActivity.class);
				
				/*
				 * change the date formt 'xxxx-x-x' to
				 * 'xxxx-xx-xx'.
				 */
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
		schoolWeekCalFragment.setCaldroidListener(listener);
		if (firsttimeFlag == true) {
			sp.edit().putBoolean("flag", false).commit();
			new AlertDialog.Builder(this).setPositiveButton("ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					startActivity(new Intent(CalendarActivity.this, SettingsActivity.class));
				}
			}).setTitle(R.string.calendar_firsttime_dialog_title)
				.setMessage(R.string.calendar_firsttime_dialog_message).show();
		}
	}
	
	/**
	 * Save current states of the Caldroid here
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		if (schoolWeekCalFragment != null) {
			schoolWeekCalFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
		}
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
//		return super.onCreateOptionsMenu(menu);
		
		// TODO set the item's intent
		Calendar cal = Calendar.getInstance();
		
		/*
		 * setting some menu items here
		 */
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
