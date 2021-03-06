package com.henryxian;

import hirondelle.date4j.DateTime;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

public class SchoolWeekAdapter extends CaldroidGridAdapter {
	private static final String TAG = "SchoolWeekAdapter";	
	
	public SchoolWeekAdapter(Context context, int month, int year,
			HashMap<String, Object> caldroidData,
			HashMap<String, Object> extraData) {
		super(context, month, year, caldroidData, extraData);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View cellView = convertView;

		// For reuse
		if (convertView == null) {
			cellView = inflater.inflate(R.layout.schoolweek_cell, null);
		}

		int topPadding = cellView.getPaddingTop();
		int leftPadding = cellView.getPaddingLeft();
		int bottomPadding = cellView.getPaddingBottom();
		int rightPadding = cellView.getPaddingRight();

		TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);
		TextView tv2 = (TextView) cellView.findViewById(R.id.tv2);
		TextView tv3 = (TextView) cellView.findViewById(R.id.tv3);

		tv1.setTextColor(Color.BLACK);
//		tv1.setBackgroundColor(resources.getColor(R.color.abs__background_holo_light));

		// Get dateTime of this cell
		DateTime dateTime = this.datetimeList.get(position);
		Resources resources = context.getResources();
		
		int day = dateTime.getDay();
		int month = dateTime.getMonth();
		int year = dateTime.getYear();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.YEAR, year);
		
		int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
		
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		dayOfWeek = dayOfWeek == 0 ? 7 : dayOfWeek;
//		
		Calendar cal2 = Calendar.getInstance();
		cal2.set(Calendar.DAY_OF_MONTH, getMinDateTime().getDay());
		cal2.set(Calendar.MONTH, getMinDateTime().getMonth() - 1);
		cal2.set(Calendar.YEAR, getMinDateTime().getYear());
		int firstSchoolWeek = cal2.get(Calendar.WEEK_OF_YEAR);
		int offset = weekOfYear - firstSchoolWeek + 1;
//		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		if (dayOfWeek == 1 && offset > 0 && offset < 21 && dateTime.getMonth() == this.month) {
			tv3.setText(String.valueOf(weekOfYear));
			tv3.setBackgroundColor(resources.getColor(R.color.xian_yellow));
			tv2.setText(String.valueOf(offset));
			tv2.setBackgroundColor(resources.getColor(R.color.xian_yellow));
		} else {
			tv3.setText("");
			tv2.setText("");
		}
		
		tv3.setTextColor(resources.getColor(R.color.caldroid_holo_blue_light));
		tv2.setTextColor(resources.getColor(R.color.caldroid_holo_blue_dark));
		
		// Set color of the dates in previous / next month
		if (dateTime.getMonth() != this.month) {
			tv1.setTextColor(resources
					.getColor(com.caldroid.R.color.caldroid_darker_gray));
			if (CaldroidFragment.disabledBackgroundDrawable == -1) {
				cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
			} else {
//				cellView.setBackgroundColor(resources.getColor(R.color.busy));
//				cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
				cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
			}
		}

		boolean shouldResetDiabledView = false;
		boolean shouldResetSelectedView = false;

		// Customize for disabled dates and date outside min/max dates
		if ((minDateTime != null && dateTime.lt(minDateTime))
				|| (maxDateTime != null && dateTime.gt(maxDateTime))
				|| (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

			tv1.setTextColor(CaldroidFragment.disabledTextColor);
			if (CaldroidFragment.disabledBackgroundDrawable == -1) {
				cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
			} else {
				cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
			}

			if (dateTime.equals(getToday())) {
				cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
			}

		} else {
			shouldResetDiabledView = true;
		}

		// Customize for selected dates
		if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
			if (CaldroidFragment.selectedBackgroundDrawable != -1) {
				cellView.setBackgroundResource(CaldroidFragment.selectedBackgroundDrawable);
			} else {
				cellView.setBackgroundColor(resources
						.getColor(com.caldroid.R.color.caldroid_sky_blue));
			}

			tv1.setTextColor(CaldroidFragment.selectedTextColor);

		} else {
			shouldResetSelectedView = true;
		}

		if (shouldResetDiabledView && shouldResetSelectedView) {
			// Customize for today
			if (dateTime.equals(getToday())) {
				cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
			} else {
				cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
			}
		}
		
		// TODO
		@SuppressWarnings("unchecked")
		HashMap<DateTime, Integer> backgroundForDateTimeMap = 
				(HashMap<DateTime, Integer>) caldroidData.get(CaldroidFragment._BACKGROUND_FOR_DATETIME_MAP);
		if (backgroundForDateTimeMap != null) {
			Integer backgroundResource = backgroundForDateTimeMap.get(dateTime);
			if (backgroundResource != null) {
				cellView.setBackgroundResource(backgroundResource.intValue());
			}
		}
		@SuppressWarnings("unchecked")
		HashMap<String, Integer> countForDateMap = (HashMap<String, Integer>)extraData.get("count");
//		Iterator<Entry<String, Integer>> iter = countForDateMap.entrySet().iterator();
//		while (iter.hasNext()) {
//			Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>)iter.next();
//			Log.d(TAG, "Date: " + entry.getKey()); 
//			Log.d(TAG, "count: " + entry.getValue());
//		}
		String today = year + "-" + month + "-" + day;
		Integer occurences = countForDateMap.get(today);
		// If the occurences is not zero, 
		// then set the corresponding color.
		if (dateTime.gteq(minDateTime) && dateTime.lteq(maxDateTime)) {
			if (occurences != null) {
				cellView.setBackgroundResource(SchoolCalHelper.getBusyColor(occurences));
			}
		}
		tv1.setText("" + dateTime.getDay());
		
//		tv2.setText("Hi");

		// Somehow after setBackgroundResource, the padding collapse.
		// This is to recover the padding
		cellView.setPadding(leftPadding, topPadding, rightPadding,
				bottomPadding);
		return cellView;
	}

}
