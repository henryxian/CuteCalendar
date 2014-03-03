package com.henryxian;

import java.util.Calendar;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class AddEventActivity extends SherlockActivity implements OnClickListener{
	private static final String TAG = AddEventActivity.class.getSimpleName();
	private static String date;
	private MyAsyncQueryHandler myAsyncQueryHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Button button = (Button)findViewById(R.id.button_add_positive);
//		button.setOnClickListener(this);
		
		setContentView(R.layout.add_event);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		TextView textView = (TextView)findViewById(R.id.text_date);
		if (bundle != null){
			date = bundle.getString("Date");
			if (date != null){
			textView.setText(date);
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
			textView.setText(date);
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
		myAsyncQueryHandler = new MyAsyncQueryHandler(this.getContentResolver());
//		myAsyncQueryHandler.startInsert(null, null, uri, initialValues);
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
}
