package com.henryxian;

import android.provider.BaseColumns;

public final class EventContract {
	public EventContract() {}
	
	public static abstract class EventEntry implements BaseColumns {
		public static final String TABLE_NAME = "events";
		public static final String COLUMN_NAME_TITLE = "title";
		public static final String COLUMN_NAME_CONTENT = "content";
		public static final String COLUMN_NAME_DATE = "date";
	}
}
