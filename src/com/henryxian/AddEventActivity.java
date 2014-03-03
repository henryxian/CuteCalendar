package com.henryxian;

import java.util.Calendar;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.henryxian.EventContract.EventEntry;

public class AddEventActivity extends SherlockActivity implements OnClickListener{
	private static final String TAG = AddEventActivity.class.getSimpleName();
	private static String date;
	private MyAsyncQueryHandler myAsyncQueryHandler;
	private TextView mTextView;
	private EditText mEditTextTitle;
	private EditText mEditTextContent;
	private Button button;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.add_event);
		
		button = (Button)findViewById(R.id.button_add_positive);
		button.setOnClickListener(new okButtonListener());
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
		
		mTextView = (TextView)findViewById(R.id.text_date);
		if (bundle != null){
			date = bundle.getString("Date");
			if (date != null){
			mTextView.setText(date);
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
			int year = cal.get(Calendar.YEAR);
			int month = cal.get(Calendar.MONTH) + 1;
			int day = cal.get(Calendar.DAY_OF_MONTH);
			
			String month2;
			String day2;
			if (month < 10) {
				month2 = "0" + month;
			} else {
				month2 = String.valueOf(month);
			}
			
			if (day < 10) {
				day2 = "0" + day;
			} else {
				day2 = String.valueOf(day);
			}
			
			date = year + "-" + month2 + "-" + day2;
			mTextView.setText(date);
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
		NavUtils.navigateUpTo(this, new Intent(this, CalendarActivity.class));
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
		
		@Override
		protected void onInsertComplete(int token, Object cookie, Uri uri) {
			// TODO Auto-generated method stub
			super.onInsertComplete(token, cookie, uri);
		}
	}
	
	private final class okButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ContentValues cv = new ContentValues();
			mEditTextTitle = (EditText)findViewById(R.id.edit_event_title);
			mEditTextContent = (EditText)findViewById(R.id.edit_event_content);
			
			cv.put(EventEntry.COLUMN_NAME_TITLE, mEditTextTitle.getText().toString());
			cv.put(EventEntry.COLUMN_NAME_CONTENT, mEditTextContent.getText().toString());
			cv.put(EventEntry.COLUMN_NAME_DATE, date);
			
			myAsyncQueryHandler = new MyAsyncQueryHandler(AddEventActivity.this.getContentResolver());
			myAsyncQueryHandler.startInsert(0, null, EventProvider.CONTENT_URI, cv);
			Toast.makeText(AddEventActivity.this, "ok", Toast.LENGTH_SHORT).show();
		}
		
	}
}
