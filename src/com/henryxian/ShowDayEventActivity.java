package com.henryxian;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
		private static final String TAG = "CursorLoaderListFragment";
		private CursorAdapter mAdapter;
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
			Log.d(TAG,  "" + year);
			
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
			
			mAdapter = new CursorAdapter(getActivity(), null) {
				
				@Override
				public View newView(Context context, Cursor cursor, ViewGroup parent) {
					// TODO Auto-generated method stub
					LayoutInflater inflater = 
							(LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
					return inflater.inflate(R.layout.event_item, null);
				}
				
				@Override
				public void bindView(View view, Context context, Cursor cursor) {
					// TODO Auto-generated method stub
					String title;
					String description;
					long startTime;
					
					title = cursor.getString(cursor.getColumnIndex(Events.TITLE));
					description = cursor.getString(cursor.getColumnIndex(Events.DESCRIPTION));
					startTime = cursor.getLong(cursor.getColumnIndex(Instances.BEGIN));
					
					Calendar cal = Calendar.getInstance();
					cal.setTimeInMillis(startTime);
					SimpleDateFormat formatter = new SimpleDateFormat("kk:mm");
					
					TextView t1 = (TextView)view.findViewById(R.id.text_show_title);
					TextView t2 = (TextView)view.findViewById(R.id.text_show_content);
					TextView t3 = (TextView)view.findViewById(R.id.text_show_date);
					
					t1.setText(title);
					t2.setText(description);
					t3.setText(formatter.format(cal.getTime()));
				}
			};
			
			setListAdapter(mAdapter);
			
			getLoaderManager().initLoader(0, null, this);
		}
		
		static final String[] projection = {
			Instances._ID,
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
