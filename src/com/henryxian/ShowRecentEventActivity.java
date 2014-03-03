package com.henryxian;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;

import com.actionbarsherlock.app.SherlockListActivity;
import com.henryxian.EventContract.EventEntry;

public class ShowRecentEventActivity extends SherlockListActivity{
	private static final String TAG = ShowRecentEventActivity.class.getSimpleName();
//	private MyAsyncQueryHanler myAsyncQueryHandler;
	private SimpleCursorAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
		String[] dataColumns = new String[]{EventEntry.COLUMN_NAME_DATE};
		int[] viewIDs = {android.R.id.text1};
		adapter = new SimpleCursorAdapter(this, R.layout.event_item, cur, dataColumns, viewIDs);
		getListView().setAdapter(adapter);
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
