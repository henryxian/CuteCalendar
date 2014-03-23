package com.henryxian;

import java.util.Calendar;

import android.util.Log;

/*
 * This is a school calendar helper class.
 */
public class SchoolCalHelper {
	// calculate the week of day which starts
	// from monday
	private static final String TAG = "SchoolCalHelper";
	
	public static int dayOfWeek(int day) {
		day = day - 1;
		day = day == 0 ? 7 : day;
		return day;
	}
	
	public static int offset(int day) {
		return dayOfWeek(day) - 1;
	}
	
	public static int[] getSchoolCalDate(int year, int month, int day, int week, int dayOfWeek) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
//		Log.d(TAG, "year: " + cal.get(Calendar.YEAR));
//		Log.d(TAG, "month: " + cal.get(Calendar.MONTH));
//		Log.d(TAG, "day: " + cal.get(Calendar.DAY_OF_MONTH));
		Log.d(TAG, "Date: " + cal.toString());
		Log.d(TAG, "year: " + year);
		Log.d(TAG, "month: " + month);
		Log.d(TAG, "day: " + day);
		cal.add(Calendar.DATE, - offset(cal.get(Calendar.DAY_OF_WEEK)));
		int count = (week - 1) * 7 + dayOfWeek - 1;
		Log.d(TAG, "count: " + count);
		cal.add(Calendar.DATE, count);
		int result[] = {
				cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH)
		};
		
		return result;
	}
}
