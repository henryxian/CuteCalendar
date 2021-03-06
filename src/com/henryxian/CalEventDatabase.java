package com.henryxian;

import com.henryxian.EventContract.EventEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class CalEventDatabase {
	private static final String TAG = CalEventDatabase.class.getSimpleName();
	
	private static final String DATABASE_NAME = "events";
	private static final int DATABASE_VERSION = 1;
	
	private final EventOpenHelper mEventOpenHelper;
	
	public CalEventDatabase(Context context) {
		mEventOpenHelper = new EventOpenHelper(context);
	}
	
	/*
	 * Get an event from database by id.
	 */
	public Cursor getEvent(Uri uri) {
		String rowId = uri.getLastPathSegment();
		String[] columns = new String[]{
				EventEntry.COLUMN_NAME_TITLE,
				EventEntry.COLUMN_NAME_CONTENT,
				EventEntry.COLUMN_NAME_DATE
		};
		String orderBy = EventEntry.COLUMN_NAME_DATE + " DESC";
		String selection = EventEntry._ID + " = ?";
		String[] selectionArgs = new String[]{rowId};
		SQLiteDatabase db = mEventOpenHelper.getReadableDatabase();
		Cursor cursor = db.query(
				EventEntry.TABLE_NAME, 
				columns, 
				selection, 
				selectionArgs, 
				null,
				null, 
				orderBy
				);
		if (cursor == null) {
			return null;
		} else if (!cursor.moveToFirst()) {
			cursor.close();
			return null;
		} else {
			return cursor;
		}
	}
	
	/*
	 * Get all events from the database.
	 */
	public Cursor getAllEvents() {
		String[] columns = new String[]{
				EventEntry._ID,
				EventEntry.COLUMN_NAME_TITLE,
				EventEntry.COLUMN_NAME_CONTENT,
				EventEntry.COLUMN_NAME_DATE
		};
		String orderBy = EventEntry.COLUMN_NAME_DATE + " DESC";
		SQLiteDatabase db = mEventOpenHelper.getReadableDatabase();
		Cursor cursor = db.query(
				EventEntry.TABLE_NAME, 
				columns, 
				null, 
				null, 
				null, 
				null, 
				orderBy
				);
		if (cursor == null) {
			return null;
		} 
//		else if (!cursor.moveToFirst()) {
//			cursor.close();
//			return null;
//		} 
		else {
			return cursor;
		}
	}
	
	//TODO 
	public Cursor getDateEventCount() {
		SQLiteDatabase db = mEventOpenHelper.getReadableDatabase();
		
		String sql = "SELECT " + EventEntry.COLUMN_NAME_DATE + " , COUNT() " + 
				" FROM " + EventEntry.TABLE_NAME + " WHERE " + EventEntry.COLUMN_NAME_DATE +
				" IN " + "( SELECT DISTINCT " + EventEntry.COLUMN_NAME_DATE + " FROM " + 
				EventEntry.TABLE_NAME + ")" + " GROUP BY " + EventEntry.COLUMN_NAME_DATE;		
//		String[] selectionArgs = {};
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor == null) {
			return null;
		} 
//		else if (!cursor.moveToFirst()) {
//			cursor.close();
//			return null;
//		} 
		else {
			return cursor;
		}
	}
	
	/*
	 * put an event into database, return -1 if failure happens.
	 */
	public long putEvent(ContentValues values) {
		SQLiteDatabase db = mEventOpenHelper.getWritableDatabase();
		long rowId = 
				db.insert(EventEntry.TABLE_NAME, EventEntry.COLUMN_NAME_CONTENT, values);
		return rowId;
	}
	
	/*
	 * update an event by id
	 */
	public int updateEvent(ContentValues values, long id) {
		SQLiteDatabase db = mEventOpenHelper.getWritableDatabase();
		String whereClause = EventEntry._ID + "= ?";
		String[] whereArgs = {String.valueOf(id)};
		int affectedCols = db.update(
				EventEntry.TABLE_NAME, 
				values, 
				whereClause, 
				whereArgs
			);
		return affectedCols;
	}
	
	/*
	 * delete all the items in events.
	 */
	public int dropTable() {
		SQLiteDatabase db = mEventOpenHelper.getWritableDatabase();
		return db.delete(EventEntry.TABLE_NAME, 
				null, 
				null);
	}
	
	/*
	 * Sqldatabase open helper class
	 */
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
