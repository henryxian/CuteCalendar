package com.henryxian;

import com.henryxian.EventContract.EventEntry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CalEventDatabase {
	private static final String TAG = CalEventDatabase.class.getSimpleName();
	
	private static final String DATABASE_NAME = "events";
	private static final int DATABASE_VERSION = 1;
	
	private final EventOpenHelper mEventOpenHelper;
	
	public CalEventDatabase(Context context) {
		mEventOpenHelper = new EventOpenHelper(context);
	}
	
	private static class EventOpenHelper extends SQLiteOpenHelper {
		private final Context mHelperContext;
		private SQLiteDatabase mDatabase;
		
		private static final String EVENTS_TABLE_CREATE = 
				"CREATE TABLE " + EventEntry.TABLE_NAME + "(" + 
				EventEntry._ID + " INTEGER PRIMARY KEY," +
				EventEntry.COLUMN_NAME_TITLE +" TEXT" + "," + 
				EventEntry.COLUMN_NAME_CONTENT + " TEXT" + "," + 
				EventEntry.COLUMN_NAME_DATE + " DATE" + 
				")";
		private static final String EVENTS_TABLE_DROP = 
				"DROP TABLE IF EXISTS " + EventEntry.TABLE_NAME;
		
		EventOpenHelper(Context context) {
			// TODO Auto-generated constructor stub
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			mHelperContext = context;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			mDatabase = db;
			mDatabase.execSQL(EVENTS_TABLE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " +
					newVersion + "which will destroy all old data");
			mDatabase.execSQL(EVENTS_TABLE_DROP);
			onCreate(db);
		}
	}
}
