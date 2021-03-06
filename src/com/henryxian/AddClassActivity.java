package com.henryxian;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.henryxian.GCalendarContract.Events;
import com.henryxian.GCalendarContract.Reminders;

public class AddClassActivity extends SherlockActivity implements 
					android.widget.AdapterView.OnItemSelectedListener{
	private static final String TAG = AddClassActivity.class.getSimpleName();
	
	private ArrayAdapter<CharSequence> adapterClassOrder;
	private ArrayAdapter<CharSequence> adapterweekDay;
	
	private EditText editTextClassName;
	private Spinner spinnerClassOrder;
	private Spinner spinnerweekDay;
	
	private Spinner spinnerStartWeek;
	private ArrayAdapter<CharSequence> adapterStartWeek;
	
	private Spinner spinnerEndWeek;
	private ArrayAdapter<CharSequence> adapterEndWeek;
	
	private Spinner spinnerReminder;
	private ArrayAdapter<CharSequence> adapterReminder;
	
	private long startMillis;
	private long endMillis;
	
	private int classOrder;
	private int weekDay;
	private int reminder;
	private int reminderMethod;
	
	private int classStartHour;
	private int classStartMin;
	private int classEndHour;
	private int classEndMin;
	
	private int startWeek;
	private int endWeek;
	
	// The start day of session, 
	// set in the sharepreference
	private String sessionStartDate;
	private int sessionStartDay;
	private int sessionStartMonth;
	private int sessionStartYear;
	
	// class flags
	private static final int FIRST_CLASS = 0;
	private static final int SECOND_CLASS = 1;
	private static final int THIRD_CLASS = 2;
	private static final int FOURTH_CLASS = 3;
	private static final int FIFTH_CLASS = 4;
	
	protected void setClass(int position) {
		switch(position) {
		case FIRST_CLASS:
			setClassHourMin(8, 30, 10, 5);
			break;
		case SECOND_CLASS:
			setClassHourMin(10, 25, 12, 0);
			break;
		case THIRD_CLASS:
			setClassHourMin(13, 50, 15, 25);
			break;
		case FOURTH_CLASS:
			setClassHourMin(15, 45, 17, 20);
			break;
		case FIFTH_CLASS:
			setClassHourMin(18, 20, 21, 0);
			break;
		default:
			setClassHourMin(8, 30, 10, 5);
			break;
		}
	}
	
	protected void setClassHourMin(int classStartHour, 
			int classStartMin, int classEndHour, int classEndMin) {
		this.classStartHour = classStartHour;
		this.classStartMin = classStartMin;
		this.classEndHour = classEndHour;
		this.classEndMin = classEndMin;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_class);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Set first class to default
		setClass(0);
		// set the session start day from the preference
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		sessionStartDate = sharedPreferences.getString("DatePreference", "2013-2-23");
		sessionStartDay = DatePickerPreference.getDay(sessionStartDate);
		sessionStartMonth = DatePickerPreference.getMonth(sessionStartDate);
		sessionStartYear = DatePickerPreference.getYear(sessionStartDate);
		
		reminderMethod = Integer.parseInt(sharedPreferences.getString("reminderMethod", "0"));
//		Toast.makeText(this, reminderMethod + "", Toast.LENGTH_SHORT).show();
//		reminderMethod = sharedPreferences.getInt("reminderMethod", 0);
//		Toast.makeText(this, reminderMethod + "", Toast.LENGTH_SHORT).show();
		
		this.editTextClassName = (EditText)findViewById(R.id.addClass_editText_className);
		// set up the class order spinner
		 this.spinnerClassOrder = (Spinner)findViewById(R.id.addClass_spinner_classOrder);
		 this.adapterClassOrder = ArrayAdapter.createFromResource(
				this, 
				R.array.class_array, 
				android.R.layout.simple_spinner_item
				);
		adapterClassOrder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerClassOrder.setAdapter(adapterClassOrder);
		spinnerClassOrder.setOnItemSelectedListener(this);
		spinnerClassOrder.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				setClass(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// set up the week day spinner
		this.spinnerweekDay = (Spinner)findViewById(R.id.addClass_spinner_weekDay);
		this.adapterweekDay = ArrayAdapter.createFromResource(
				this, 
				R.array.weekday_array, 
				android.R.layout.simple_spinner_item
				);
		adapterweekDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerweekDay.setAdapter(adapterweekDay);
		spinnerweekDay.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				AddClassActivity.this.weekDay = position + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		// Set up reminder spinner
		spinnerReminder = (Spinner)findViewById(R.id.addClass_spinner_reminder);
		adapterReminder = ArrayAdapter.createFromResource(
				this, R.array.reminder_array, 
				android.R.layout.simple_spinner_item
				);
		adapterReminder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerReminder.setAdapter(adapterReminder);
		spinnerReminder.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				AddClassActivity.this.reminder = position;
				switch (position) {
				case 1:
					AddClassActivity.this.reminder = 1;
					break;
				case 2:
					AddClassActivity.this.reminder = 15;
					break;
				case 3:
					AddClassActivity.this.reminder = 60;
					break;
				default:
					AddClassActivity.this.reminder = 0;
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		// Set up the start-week spinner
		spinnerStartWeek = (Spinner)findViewById(R.id.addClass_spinner_classStartWeek);
		adapterStartWeek = ArrayAdapter.createFromResource(
				this, 
				R.array.schoolweek_array, 
				android.R.layout.simple_spinner_item
			);
		adapterStartWeek.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerStartWeek.setAdapter(adapterStartWeek);
		spinnerStartWeek.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				AddClassActivity.this.startWeek = position + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//Set up the end-week spinner
		spinnerEndWeek = (Spinner)findViewById(R.id.addClass_spinner_classEndWeek);
		adapterEndWeek = ArrayAdapter.createFromResource(
				this, 
				R.array.schoolweek_array, 
				android.R.layout.simple_spinner_item
			);
		adapterEndWeek.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerEndWeek.setAdapter(adapterEndWeek);
		spinnerEndWeek.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				AddClassActivity.this.endWeek = position + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpTo(this, new Intent(this, CalendarActivity.class));
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		this.classOrder = position;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}	
	
	public void confirmAddClass(View view) {
		String className = editTextClassName.getText().toString();
		if (className.length() == 0 || (startWeek > endWeek)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			if (className.length() == 0){
				builder.setMessage(R.string.addClass_alertDialog_messageClassNull);
			}
			if (startWeek > endWeek) {
				builder.setMessage(R.string.addClass_alertDialog_messageOutOfEndWeek);
			}
			builder.setTitle(R.string.addClass_alertDialog_title)
				.setPositiveButton(R.string.addClass_alertDialog_dismiss, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
		}
		else {
			// TODO insert class
			int date[] = SchoolCalHelper.getSchoolCalDate(
					sessionStartYear, 
					sessionStartMonth, 
					sessionStartDay, 
					startWeek, 
					weekDay
				);
			Log.d(TAG, "sessionStartDate: " + sessionStartDate);
			Log.d(TAG, "sessionStartDay: " + sessionStartDay);
			Log.d(TAG, "sessionStartMonth: " + sessionStartMonth);
			Log.d(TAG, "sessionStartYear: " + sessionStartYear);
			
			Calendar calStart = Calendar.getInstance();
			Calendar calEnd = Calendar.getInstance();
			calStart.set(date[0], date[1] - 1, date[2], classStartHour, classStartMin);
			Log.d(TAG, "date[0]: " + date[0]);
			Log.d(TAG, "date[1]: " + date[1]);
			Log.d(TAG, "date[2]: " + date[2]);
			
			calEnd.set(date[0], date[1] - 1, date[2], classEndHour, classEndMin);
			
			startMillis = calStart.getTimeInMillis();
			endMillis = calEnd.getTimeInMillis();
			
			ContentValues cv = new ContentValues();
			cv.put(Events.CALENDAR_ID, 1);
			cv.put(Events.TIMEZONE, TimeZone.getDefault().getID());
			cv.put(Events.TITLE, className);
			cv.put(Events.DTSTART, startMillis);
			cv.put(Events.DTEND, endMillis);
			// The frequency is weekly, and repeat several times from the 
			// start time the event is set.
			String rrule = "FREQ=WEEKLY;COUNT=" + (endWeek - startWeek + 1);
			cv.put(Events.RRULE, rrule);
			
			MyAsyncQeuryHandler handler = new MyAsyncQeuryHandler(getContentResolver());
			handler.startInsert(
						1, 
						null, 
						Events.URI, 
						cv
					);
			
//			Toast.makeText(this, 
//					" ClassName " + className +
//					" classOrder: " + classOrder + 
//					" weekDay " + weekDay +
//					" startWeek " + startWeek +
//					" endWeek " + endWeek +
//					" startHour " + classStartHour +
//					" startMin " + classStartMin +
//					" endHour " + classEndHour +
//					" endMin " + classEndMin,
//					Toast.LENGTH_LONG).show();
			Toast.makeText(this, "OK!", Toast.LENGTH_SHORT).show();
		}
	}
	
	private final class MyAsyncQeuryHandler extends AsyncQueryHandler {
		public MyAsyncQeuryHandler(ContentResolver contentResolver) {
			// TODO Auto-generated constructor stub
			super(contentResolver);
		}
		
		@Override
		protected void onInsertComplete(int token, Object cookie, Uri uri) {
			// TODO navigate back to the calendar activity
			super.onInsertComplete(token, cookie, uri);
			reminder = AddClassActivity.this.reminder;
			if (reminder != 0) {
				long eventId = ContentUris.parseId(uri);
				ContentValues cv = new ContentValues();
				cv.put(Reminders.EVENT_ID, eventId);
				cv.put(Reminders.MINUTES, reminder);
				cv.put(Reminders.METHOD, reminderMethod);
				AddClassActivity.this.getContentResolver()
					.insert(Reminders.URI, cv);
			}
		}
	}
}
