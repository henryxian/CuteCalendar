package com.henryxian;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.henryxian.GCalendarContract.Events;
import com.henryxian.GCalendarContract.Instances;

@SuppressLint("NewApi")
public class ShowDayEventActivity extends SherlockFragmentActivity {
	
	private int year;
	private int month;
	private int day;
	
	@Override
	protected void onCreate(Bundle saveInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(saveInstanceState);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		
		FragmentManager fm = getSupportFragmentManager();
		
		if (fm.findFragmentById(android.R.id.content) == null) {
			CursorLoaderListFragment list = new CursorLoaderListFragment();
			list.setArguments(bundle);
			fm.beginTransaction().add(android.R.id.content, list).commit();
		}
	}
	
	public static class CursorLoaderListFragment extends SherlockListFragment 
			implements LoaderManager.LoaderCallbacks<Cursor> {
	
		private SimpleCursorAdapter mAdapter;
		private int year;
		private int month;
		private int day;
		
		@SuppressWarnings("deprecation")
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			
			Bundle bundle = getArguments();
			day = bundle.getInt("day");
			month = bundle.getInt("month");
			year = bundle.getInt("year");
			Log.d("showfragment ",  "" + year);
			
			String[] from = {
					Instances.BEGIN,
					Events.TITLE,
					Events.DESCRIPTION
				};
			
			int[] to = {
					R.id.text_show_date, 
					R.id.text_show_title,
					R.id.text_show_content
				};
			
			mAdapter = new SimpleCursorAdapter(
					getActivity(),
					R.layout.event_item,
//					android.R.layout.simple_list_item_2,
					null, 
					from, 
					to,
					0
				);
			
			setListAdapter(mAdapter);
			
			getLoaderManager().initLoader(0, null, this);
		}
		
		static final String[] projection = {
			"_id",
			Instances.BEGIN,
			Events.TITLE,
			Events.DESCRIPTION
		};
		
		@Override
		public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
			// TODO Auto-generated method stub
			Uri baseUri = Instances.CONTENT_URI;
			Uri.Builder builder = baseUri.buildUpon();
			Calendar startTime = Calendar.getInstance();
			Calendar endTime = Calendar.getInstance();
			startTime.set(year, month - 1, day, 0, 0);
			Log.d("showfragment ", startTime.toString());
			endTime.set(year, month - 1, day, 23, 59);
			Log.d("showfragment ", endTime.toString());
			ContentUris.appendId(builder, startTime.getTimeInMillis());
			ContentUris.appendId(builder, endTime.getTimeInMillis());
			
			return new CursorLoader(
					getActivity(), 
					builder.build(), 
					projection, 
					null, 
					null, 
					null
				);
		}
	
		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			// Swap the new cursor in. The framework will take care of closing
			// the old cursor once we return.
			mAdapter.swapCursor(data);
		}
	
		@Override
		public void onLoaderReset(Loader<Cursor> loadrer) {
			// This is called when the last cursor provided to onLoadFinished()
			// is about to be closed. We need to make sure we are no longer using
			// it.
			mAdapter.swapCursor(null);
		}
		}
}
