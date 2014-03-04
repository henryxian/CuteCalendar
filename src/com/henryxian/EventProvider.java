package com.henryxian;

import java.io.ObjectOutputStream.PutField;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

// provide access to database
public class EventProvider extends ContentProvider{
	public static final String TAG = EventProvider.class.getSimpleName();
	
	// define authority of the contentprovider
	public static String AUTHORITY = "com.henryxian.EventProvider";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/events");
	private CalEventDatabase mCalEventDatabase;
	
	// UriMatcher stuff
	private static final UriMatcher sURIMatcher = buildUriMatcher();
	private static final int EVENTS = 1;
	private static final int SPECIFIED_EVENT = 2;
	
	private static UriMatcher buildUriMatcher() {
		UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "events", EVENTS);
		uriMatcher.addURI(AUTHORITY, "events/#", SPECIFIED_EVENT);
		return uriMatcher;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}
	
	/*
	 * Provide simple insert operation.
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		Uri eventUri;
		switch(sURIMatcher.match(uri)) {
		case EVENTS:
			long rowId = mCalEventDatabase.putEvent(values);
			if (rowId > 0) {
				eventUri = ContentUris.withAppendedId(uri, rowId);
				return eventUri;
			}
		}
		throw new SQLException("Failed to insert row" + uri);
	}

	/*
	 * obtain an db instance.
	 */
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mCalEventDatabase = new CalEventDatabase(getContext());
		return true;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		Cursor result;
		// TODO Auto-generated method stub
		switch(sURIMatcher.match(uri))
		{
			case EVENTS:
				return mCalEventDatabase.getAllEvents();
			case SPECIFIED_EVENT:
				return mCalEventDatabase.getEvent(uri);
		}
		throw new SQLException("Query Exception");
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		long id;
		switch(sURIMatcher.match(uri))
		{
		case SPECIFIED_EVENT:
				id = ContentUris.parseId(uri);
				return mCalEventDatabase.updateEvent(values, id);
		}
		throw new SQLException("Update Exception");
	}
	
}
