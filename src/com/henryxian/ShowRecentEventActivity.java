package com.henryxian;

import java.util.Calendar;
import java.util.HashMap;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.henryxian.EventContract.EventEntry;

public class ShowRecentEventActivity extends SherlockListActivity{
	private static final String TAG = ShowRecentEventActivity.class.getSimpleName();
	private CursorAdapter adapter;
	
	static class ViewHolder {
		TextView description;
		TextView title;
		TextView timestamp;
		int position;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		long calendarId = 1;
		String selection = " calendar_id = ? ";
		String[] selectionArgs = 
				new String[]{
				Long.toString(calendarId)
		};
		
		Uri uri = Uri.parse("content://com.android.calendar/events");
		String[] projection = 
				new String[] {
				"_id",
				"title",
				"dtstart",
				"description"
		};
		
		String[] dataColumns = new String[]{
				projection[1], 
				projection[2],
				projection[3]
				};
		
		int[] viewIDs = {
				R.id.text_show_title,  
				R.id.text_show_date,
				R.id.text_show_content
				};
		
		Cursor cur = managedQuery(
				uri, 
				projection, 
				selection, 
				selectionArgs, 
				null
				);
		
//		adapter = new SimpleCursorAdapter(this, R.layout.event_item, cur, dataColumns, viewIDs);
		adapter = new CursorAdapter(this, cur) {
			@Override
			public View newView(Context context, Cursor cursor, ViewGroup parent) {
				// TODO Auto-generated method stub
				ViewHolder holder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater)context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View inflate = inflater.inflate(R.layout.event_item, null);
				holder.description = (TextView)parent.findViewById(R.id.text_show_content);
				holder.title = (TextView)parent.findViewById((R.id.text_show_title));
				holder.timestamp = (TextView)parent.findViewById(R.id.text_show_date);
				inflate.setTag(holder);
				return inflate;
			}
			
			@Override
			public void bindView(View view, Context context, Cursor cursor) {
				String title;
				String description;
				long timestamp;
				// TODO Auto-generated method stub
				ViewHolder holder = (ViewHolder)view.getTag();
				title = cursor.getString(cursor.getColumnIndex("title"));
				Log.d(TAG, "this is the title: " + title);
				description =  cursor.getString(cursor.getColumnIndex("description"));
				Log.d(TAG, "this is the description: " + description);
				timestamp = cursor.getLong(cursor.getColumnIndex("dtstart"));
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(timestamp);
				String time = cal.get(Calendar.YEAR) + "-" 
						+ (cal.get(Calendar.MONTH) + 1) + "-" 
						+ cal.get(Calendar.DAY_OF_MONTH);
				
				// set the Textviews in the cell to display content
				TextView t1 = (TextView)view.findViewById(R.id.text_show_content);
				TextView t2 = (TextView)view.findViewById(R.id.text_show_date);
				TextView t3 = (TextView)view.findViewById(R.id.text_show_title);
				Log.d(TAG, "t1: " + t1);
				t1.setText(description);
				t2.setText(time);
				t3.setText(title);
			}

		};
		
		// set the listview adapter
		getListView().setAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				ListView listView = (ListView)parent;
				Cursor cursor = (Cursor)listView.getItemAtPosition(position);
				String description = String.valueOf(cursor.getString(cursor.getColumnIndex("description")));
				if (description != "null") {
					Toast.makeText(ShowRecentEventActivity.this, description, Toast.LENGTH_SHORT).show();
				}
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
		return true;
	}
}
