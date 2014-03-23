package com.henryxian;

import android.net.Uri;

public final class GCalendarContract {
	private GCalendarContract (){}
	
	public static abstract class Calendars {
		public static final Uri URI = Uri.parse("content://com.android.calendar/calendars");
		
		public static final String _ID = "_id";
		public static final String NAME = "name";
		public static final String CALENDAR_DISPLAY_NAME = "calendar_displayName";
	}
	
	public static abstract class Events {
		public static final Uri URI = Uri.parse("content://com.android.calendar/events");
		
		public static final String _ID = "_id";
		public static final String CALENDAR_ID = "calendar_id";
		public static final String TITLE = "title";
		public static final String DESCRIPTION = "description";
		public static final String DTSTART = "dtstart";
		public static final String DTEND = "dtend";
		public static final String RRULE = "rrule";
	}
	
	public static abstract class Reminders {
		// Reminder methods
		public static final Uri URI = Uri.parse("content://com.android.calendar/reminders");
		
		public static final int METHOD_DEFAULT = 0;
		public static final int METHOD_ALERT = 1;
		public static final int METHOD_EMAIL = 2;
		public static final int METHOD_SMS = 3;
		public static final int METHOD_ALARM = 4;
		
		public static final String EVENT_ID = "event_id";
		public static final String MINUTES = "minutes";
		public static final String METHOD = "method";
	}
	
	public static abstract class Instances {
		public static final Uri URI = Uri.parse("content://com.android.calendar/instances");
		
		public static final String EVENT_ID = "event_id";
		public static final String BEGIN = "begin";
		public static final String END = "end";
		public static final String END_DAY = "endDay";
		public static final String END_MINUTE = "endMinute";
		public static final String START_DAY = "startDay";
		public static final String START_MINUTE = "startMinute";
	}
}
