package com.henryxian;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

public class AddEventActivity extends SherlockFragmentActivity implements 
	OnClickListener, TimePickerFragment.OnTimeChangedListener{
	private static final String TAG = AddEventActivity.class.getSimpleName();
	private static String date;
	private MyAsyncQueryHandler myAsyncQueryHandler;
	private TextView mTextDate;
	private TextView mTextTime;
	private EditText mEditTextTitle;
	private EditText mEditTextContent;
	private Button button;
	private TimePickerFragment fragment;
	private int dialogHour;
	private int dialogMinute;
	private int day;
	private int month;
	private int year;
	
	private Spinner spinnerReminder;
	private ArrayAdapter<CharSequence> adapterReminder;
	private Spinner spinnerReccur;
	private ArrayAdapter<CharSequence> adapterReccur;
	private int reminder;
	private int reccur;
	
	public void setDialogHour(int hour) {
		this.dialogHour = hour;
	}
	
	public void setDialogMinute(int minute) {
		this.dialogMinute = minute;
	}
	
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
		
		
//		button.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Toast.makeText(AddEventActivity.this, "OK", Toast.LENGTH_LONG).show();
//			}
//		});
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		mTextDate = (TextView)findViewById(R.id.text_date);
		mTextTime = (TextView)findViewById(R.id.text_time);
		
		if (bundle != null){
			date = bundle.getString("Date");
			
			// parse the date passing from the intent
			day = DatePickerPreference.getDay(date);
			month = DatePickerPreference.getMonth(date);
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
			month = cal.get(Calendar.MONTH);
			day = cal.get(Calendar.DAY_OF_MONTH);
			dialogHour = cal.get(Calendar.HOUR_OF_DAY);
			dialogMinute = cal.get(Calendar.MINUTE);
			
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
			mTextTime.setText("•rég£º " + dialogHour + ":" + dialogMinute);
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
			cv.put("event_id", eventId);
			cv.put("minutes", 0);
			// DEFAULT METHOD
			cv.put("method", 4);
			AddEventActivity.this.getContentResolver()
				.insert(
					Uri.parse("content://com.android.calendar/reminders"),
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
	
	public void showTimePickerDialog(View v) {
//		Toast.makeText(this, "onclicktime", 1).show();
		if (fragment == null) {
			fragment = new TimePickerFragment();
		}
		fragment.show(getSupportFragmentManager(), "timepicker");
		
	}
	
	private final class okButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ContentValues cv = new ContentValues();
			mEditTextTitle = (EditText)findViewById(R.id.edit_event_title);
			mEditTextContent = (EditText)findViewById(R.id.edit_event_content);
			Uri uri = Uri.parse("content://com.android.calendar/events");
			
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
			} else {
//				cv.put(EventEntry.COLUMN_NAME_TITLE, mEditTextTitle.getText().toString());
//				cv.put(EventEntry.COLUMN_NAME_CONTENT, mEditTextContent.getText().toString());
//				cv.put(EventEntry.COLUMN_NAME_DATE, date);
//				myAsyncQueryHandler = new MyAsyncQueryHandler(AddEventActivity.this.getContentResolver());
//				myAsyncQueryHandler.startInsert(0, null, EventProvider.CONTENT_URI, cv);
//				Toast.makeText(AddEventActivity.this, R.string.toast_ok, Toast.LENGTH_SHORT).show();
				// TODO
				long calId = 1;
				long startMillis = 0;
				long endMillis = 0;
				Calendar cal = Calendar.getInstance();
				cal.set(year, month, day, dialogHour, dialogMinute);
				startMillis = cal.getTimeInMillis();
				cv.put("calendar_id", calId);
				cv.put("dtstart", startMillis);
				cv.put("description", mEditTextContent.getText().toString());
				cv.put("title", mEditTextTitle.getText().toString());
				myAsyncQueryHandler = new MyAsyncQueryHandler(AddEventActivity.this.getContentResolver());
				myAsyncQueryHandler.startInsert(
						0, 
						null, 
						uri, 
						cv
					);
				Toast.makeText(AddEventActivity.this, "ok!", Toast.LENGTH_SHORT).show();
		}
		}
	}

	@Override
	public void onTimeChanged(int hour, int minute) {
		// TODO Auto-generated method stub
		dialogHour = hour;
		dialogMinute = minute;
		mTextTime.setText("•rég: " + dialogHour + ":" + dialogMinute); 
//		Toast.makeText(this, String.valueOf(hour), Toast.LENGTH_SHORT).show();;
	}
}
