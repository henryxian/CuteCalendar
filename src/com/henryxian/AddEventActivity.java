package com.henryxian;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.henryxian.GCalendarContract.Events;
import com.henryxian.GCalendarContract.Reminders;

public class AddEventActivity extends SherlockFragmentActivity implements 
	OnClickListener, TimePickerFragment.OnTimeChangedListener{
	private static final String TAG = AddEventActivity.class.getSimpleName();
	private static String date;
	private MyAsyncQueryHandler myAsyncQueryHandler;
	private TextView mTextDate;
	private TextView mTextStartTime;
	private TextView mTextEndTime;
	private EditText mEditTextTitle;
	private EditText mEditTextContent;
	private Button button;
	private TimePickerFragment startTimePicker;
	private TimePickerFragment endTimePicker;
	private int startHour;
	private int startMinute;
	private int endHour;
	private int endMinute;
	private int day;
	private int month;
	private int year;
	
	private Spinner spinnerReminder;
	private ArrayAdapter<CharSequence> adapterReminder;
	
	private Spinner spinnerReccur;
	private ArrayAdapter<CharSequence> adapterReccur;
	
	private int reminder;
	private int reccur;
	
	public int getReminder() {
		return reminder;
	}
	
	public int getReccur() {
		return reccur;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.add_event);
		
		button = (Button)findViewById(R.id.button_add_positive);
		button.setOnClickListener(new okButtonListener());
		
		// Set up the reminder spinner
		spinnerReminder = (Spinner)findViewById(R.id.addEvent_spinner_reminder);
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
				AddEventActivity.this.reminder = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		
		// Set up the reccurence spinner
		spinnerReccur = (Spinner)findViewById(R.id.addEvent_spinner_reccur);
		adapterReccur = ArrayAdapter.createFromResource(
				this, 
				R.array.reccur_array, 
				android.R.layout.simple_spinner_item
			);
		adapterReccur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerReccur.setAdapter(adapterReccur);
		spinnerReccur.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				AddEventActivity.this.reccur = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		mTextDate = (TextView)findViewById(R.id.text_date);
		mTextStartTime = (TextView)findViewById(R.id.addEvent_text_startTime);
		mTextEndTime = (TextView)findViewById(R.id.addEvent_text_endTime);
		
		if (bundle != null){
			date = bundle.getString("Date");
			
			// parse the date passing from the intent
			day = DatePickerPreference.getDay(date);
			// NOTICE here
			month = DatePickerPreference.getMonth(date) - 1;
			year = DatePickerPreference.getYear(date);
			
			if (date != null){
			mTextDate.setText(date);
			} 
			else {
//				Calendar cal = Calendar.getInstance();
//				int year = cal.get(Calendar.YEAR);
//				int month = cal.get(Calendar.MONTH) + 1;
//				int day = cal.get(Calendar.DAY_OF_MONTH);
//				textView.setText(cal.toString());
			} 
		} else {
			Calendar cal = Calendar.getInstance();
			year = cal.get(Calendar.YEAR);
			// NOTICE here
			month = cal.get(Calendar.MONTH);
			day = cal.get(Calendar.DAY_OF_MONTH);
			startHour = cal.get(Calendar.HOUR_OF_DAY);
			startMinute = cal.get(Calendar.MINUTE);
			endHour = startHour;
			endMinute = startMinute;
//			
			String month2;
			String day2;
			if (month < 9) {
				month2 = "0" + (month + 1);
			} else {
				month2 = String.valueOf(month + 1);
			}
			
			if (day < 10) {
				day2 = "0" + day;
			} else {
				day2 = String.valueOf(day);
			}
			
			date = year + "-" + month2 + "-" + day2;
			mTextDate.setText(date);
//			mTextStartTime.setText(getResources().getString(R.string.addEvent_text_startTime)
//					+ " " + startHour + ":" + startMinute);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(R.string.menu_settings)
			.setIntent(new Intent(this, SettingsActivity.class))
			.setIcon(R.drawable.ic_action_settings)
			.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
		case android.R.id.home:
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(
					this.getCurrentFocus().getWindowToken(), 
					// TODO maybe a bug here. 
					InputMethodManager.RESULT_UNCHANGED_SHOWN);
			
			NavUtils.navigateUpTo(this, new Intent(this, CalendarActivity.class));
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	private final class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
			// TODO Auto-generated constructor stub
		}
		
		// TODO
		public void insertReminder(long eventId, int minutes) {
			ContentValues cv = new ContentValues();
			cv.put(Reminders.EVENT_ID, eventId);
			cv.put(Reminders.MINUTES, minutes);
			// METHOD DEFAULT
			cv.put(Reminders.METHOD, Reminders.METHOD_DEFAULT);
			AddEventActivity.this.getContentResolver()
				.insert(
					Reminders.URI,
					cv
				);
		}
		@Override
		protected void onInsertComplete(int token, Object cookie, Uri uri) {
			// TODO Auto-generated method stub
			super.onInsertComplete(token, cookie, uri);
			Log.d(TAG, uri.toString());
			long eventId = ContentUris.parseId(uri);
			switch(AddEventActivity.this.getReminder())
			{
			case 0:
				break;
			case 1:
				insertReminder(eventId, 1);
				break;
			case 2:
				insertReminder(eventId, 15);
				break;
			default:
				insertReminder(eventId, 60);
				break;
			};
		}
	}
	
	public void showStartTimePickerDialog(View v) {
//		Toast.makeText(this, "onclicktime", 1).show();
		if (startTimePicker == null) {
			startTimePicker = new TimePickerFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("flag", 1);
			startTimePicker.setArguments(bundle);
		}
		startTimePicker.show(getSupportFragmentManager(), "starttimepicker");
	}
	
	public void showEndTimePickerDialog(View v) {
		if (endTimePicker == null) {
			endTimePicker = new TimePickerFragment();
			Bundle bunble = new Bundle();
			bunble.putInt("flag", 0);
			endTimePicker.setArguments(bunble);
		}
		endTimePicker.show(getSupportFragmentManager(), "endtimepicker");
	}
	
	private final class okButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ContentValues cv = new ContentValues();
			mEditTextTitle = (EditText)findViewById(R.id.edit_event_title);
			mEditTextContent = (EditText)findViewById(R.id.edit_event_content);
			
			if (mEditTextTitle.getText().length() == 0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
				builder.setTitle(R.string.alertdialog_title)
					.setMessage(R.string.alertdialog_message)
					.setPositiveButton(R.string.alertdialog_dismiss, 
							new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					}).show();
			} else if ((endHour < startHour) || (endHour == startHour && endMinute < startMinute)) {
				AlertDialog.Builder builder = new AlertDialog.Builder(AddEventActivity.this);
				builder.setTitle(R.string.addEvent_alertDialog_wrongDuration_title)
					.setMessage(R.string.addEvent_alertDialog_wrongDuration_message)
					.setPositiveButton(R.string.addEvent_alertDialog_wrongDuration_positive, 
							new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					}).show();
			} else {
				long calId = 1;
				long startMillis = 0;
				// TODO endmillis insert
				// TODO add recurrence rule
				long endMillis = 0;
				Calendar calStart = Calendar.getInstance();
				Calendar calEnd = Calendar.getInstance();
				calStart.set(year, month, day, startHour, startMinute);
				calEnd.set(year, month, day, endHour, endMinute);
				startMillis = calStart.getTimeInMillis();
				endMillis = calEnd.getTimeInMillis();
				String rrule = reccur == 0 ? "FREQ=WEEKLY" : "FREQ=MONTHLY";
				
				cv.put(Events.CALENDAR_ID, calId);
				cv.put(Events.DTSTART, startMillis);
				cv.put(Events.DTEND, endMillis);
				cv.put(Events.DESCRIPTION, mEditTextContent.getText().toString());
				cv.put(Events.TITLE, mEditTextTitle.getText().toString());
				// TODO rrule
				cv.put(Events.RRULE, rrule);
				myAsyncQueryHandler = new MyAsyncQueryHandler(AddEventActivity.this.getContentResolver());
				myAsyncQueryHandler.startInsert(
						0, 
						null, 
						Events.URI, 
						cv
					);
				Toast.makeText(AddEventActivity.this, "ok!", Toast.LENGTH_SHORT).show();
		}
		}
	}

	@Override
	public void onTimeChanged(int hour, int minute, int flag) {
		// TODO Auto-generated method stub
		Resources res = getResources();
		SimpleDateFormat formatter = new SimpleDateFormat("kk:mm");
		if (flag == 1) {
			startHour = hour;
			Log.d(TAG, "startHour: " + startHour);
			startMinute = minute;
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, startHour);
			cal.set(Calendar.MINUTE, startMinute);
			mTextStartTime.setText(res.getString(R.string.addEvent_text_startTime) 
					+ formatter.format(cal.getTime()));
		} else {
			endHour = hour;
			endMinute = minute;
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, endHour);
			cal.set(Calendar.MINUTE, endMinute);
			mTextEndTime.setText(res.getString(R.string.addEvent_text_endTime)
					+ formatter.format(cal.getTime()));
		}
	}
}
