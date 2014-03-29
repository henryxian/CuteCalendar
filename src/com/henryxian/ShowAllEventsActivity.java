package com.henryxian;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.AsyncQueryHandler;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.MenuItem;
import com.henryxian.GCalendarContract.Events;
import com.henryxian.GCalendarContract.Instances;

@SuppressLint("NewApi")
public class ShowAllEventsActivity extends SherlockFragmentActivity {
	
	@Override
	protected void onCreate(Bundle saveInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(saveInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String date = sp.getString("DatePreference", "2013-2-23");
		int year = DatePickerPreference.getYear(date);
		int month = DatePickerPreference.getMonth(date);
		int day = DatePickerPreference.getDay(date);
		Bundle args = new Bundle();
		args.putInt("year", year);
		args.putInt("month", month);
		args.putInt("day", day);
		
		FragmentManager fm = getSupportFragmentManager();
		
		if (fm.findFragmentById(android.R.id.content) == null) {
			CursorLoaderListFragment list = new CursorLoaderListFragment();
			list.setArguments(args);
			fm.beginTransaction().add(android.R.id.content, list).commit();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpTo(this, new Intent(this, CalendarActivity.class));
		}
		
		return super.onOptionsItemSelected(item);
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
			
			String[] from = {
					Events._ID,
					Events.DTSTART,
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
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					
					TextView t1 = (TextView)view.findViewById(R.id.text_show_title);
					TextView t2 = (TextView)view.findViewById(R.id.text_show_content);
					TextView t3 = (TextView)view.findViewById(R.id.text_show_date);
					
					t1.setText(title);
					t2.setText(description);
					t3.setText(formatter.format(cal.getTime()));
				}
			};
			
			setListAdapter(mAdapter);
			getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						int position, long id) {
					Cursor c = (Cursor)parent.getItemAtPosition(position);
					long eventId = c.getLong(c.getColumnIndex(Instances.EVENT_ID));
					Uri deleteUri = ContentUris.withAppendedId(Events.URI, eventId);
//					Toast.makeText(getActivity(), "id: " + eventId, Toast.LENGTH_SHORT).show();
					new AsyncQueryHandler(getActivity().getContentResolver()) {
						@Override
						protected void onDeleteComplete(int token, Object cookie,
								int result) {
							// TODO Auto-generated method stub
							super.onDeleteComplete(token, cookie, result);
							
							Toast.makeText(getActivity(), R.string.showDayEVent_deleteFinished, Toast.LENGTH_SHORT).show();
						}
					}.startDelete(
							4, 
							null, 
							deleteUri, 
							null, 
							null
						);
					CursorLoaderListFragment fragment = CursorLoaderListFragment.this;
					fragment.getLoaderManager().restartLoader(0, null, fragment);
					
					return true;
				}
			});
			
			getLoaderManager().initLoader(0, null, this);
		}
	
		static final long calendarId = 1;
		static final String selection = " calendar_id = ? ";
		static final String[] selectionArgs = 
				new String[]{
				Long.toString(calendarId)
		};
		
		static final String sortOrder = Instances.BEGIN + " DESC";
		
		static final String[] projection = 
						new String[] {
						Instances._ID,
						Instances.EVENT_ID,
						Events.TITLE,
						Events.DESCRIPTION,
						Instances.BEGIN
				};
				
		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// TODO
			Cursor cursor = (Cursor)l.getItemAtPosition(position);
			String description = cursor.getString(cursor.getColumnIndex(Events.DESCRIPTION));
			if (description != null) {
				Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
			}
		}
		
		@Override
		public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
			Uri baseUri = Instances.CONTENT_URI;
			Uri.Builder builder = baseUri.buildUpon();
			
			Bundle date = getArguments();
			int year = date.getInt("year");
			int month = date.getInt("month") - 1;
			int day = date.getInt("day");
			Calendar calStart = Calendar.getInstance();
			calStart.set(year, month, day, 0, 0);
			int dayOfWeek = calStart.get(Calendar.DAY_OF_WEEK);
			calStart.add(Calendar.DATE, - SchoolCalHelper.offset(dayOfWeek));
			Calendar calEnd = (Calendar)calStart.clone();
			calEnd.add(Calendar.DATE, 139);
			calEnd.set(Calendar.HOUR_OF_DAY, 23);
			calEnd.set(Calendar.MINUTE, 59);
			Log.d(TAG, "calStart: " + calStart.toString());
			Log.d(TAG, "calEnd: " + calEnd.toString());
			ContentUris.appendId(builder, calStart.getTimeInMillis());
			ContentUris.appendId(builder, calEnd.getTimeInMillis());
			
			return new CursorLoader(
					getActivity(), 
					builder.build(),
					projection, 
					null, 
					null,
					sortOrder
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
