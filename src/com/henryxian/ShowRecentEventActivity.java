package com.henryxian;

import java.util.HashMap;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.henryxian.EventContract.EventEntry;

public class ShowRecentEventActivity extends SherlockListActivity{
	private static final String TAG = ShowRecentEventActivity.class.getSimpleName();
//	private MyAsyncQueryHanler myAsyncQueryHandler;
	private SimpleCursorAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Cursor cur = managedQuery(EventProvider.CONTENT_URI, null, null, null, null);
		
//		myAsyncQueryHandler = new MyAsyncQueryHanler(this.getContentResolver());
//		myAsyncQueryHandler
//			.startQuery(
//				0, 
//				null, 
//				EventProvider.CONTENT_URI, 
//				null, 
//				null, 
//				null, 
//				null
//			);
		String[] dataColumns = new String[]{
				EventEntry.COLUMN_NAME_TITLE, 
				EventEntry.COLUMN_NAME_DATE,
				EventEntry.COLUMN_NAME_CONTENT
				};
		
		int[] viewIDs = {
				R.id.text_show_title,  
				R.id.text_show_date,
				R.id.text_show_content
				};
		adapter = new SimpleCursorAdapter(this, R.layout.event_item, cur, dataColumns, viewIDs);
		
		getListView().setAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				ListView listView = (ListView)parent;
				Cursor cursor = (Cursor)listView.getItemAtPosition(position);
				String content = String.valueOf(cursor.getString(cursor.getColumnIndex(EventEntry.COLUMN_NAME_CONTENT)));
				if (!"".equals(content)) {
					Toast.makeText(ShowRecentEventActivity.this, content, Toast.LENGTH_SHORT).show();
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
//	private final class MyAsyncQueryHanler extends AsyncQueryHandler {
//
//		public MyAsyncQueryHanler(ContentResolver cr) {
//			super(cr);
//			// TODO Auto-generated constructor stub
//		}
//		@Override
//		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
//			// TODO Auto-generated method stub
//			
//			String[] dataColumns = new String[]{EventEntry.COLUMN_NAME_TITLE};
//			int[] viewIDs = {android.R.id.text1};
//			ShowRecentEventActivity.this.adapter = new SimpleCursorAdapter(
//					ShowRecentEventActivity.this, 
//					R.layout.event_item, 
//					cursor, 
//					dataColumns, 
//					viewIDs
//				);
//		}
//	}
}
