package com.henryxian;
import java.util.Calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

public class DatePickerPreference extends DialogPreference {
	private final static String TAG = DatePickerPreference.class.getSimpleName();
	
	private DatePicker datePicker;
	private Calendar cal;
	private int year;
	private int month;
	private int day;
	
	public DatePickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		setPositiveButtonText(R.string.dialog_positive_button);
		setNegativeButtonText(R.string.dialog_negative_button);
	}
	
	public static int getYear(String date) {
		String[] pieces = date.split("-");
		return Integer.parseInt(pieces[0]);
	}
	
	public static int getMonth(String date) {
		String[] pieces = date.split("-");
		return Integer.parseInt(pieces[1]);
	}
	
	public static int getDay(String date) {
		String[] pieces = date.split("-");
		return Integer.parseInt(pieces[2]);
	}
	
	@Override
	protected View onCreateDialogView() {
		datePicker = new DatePicker(getContext());
//		cal = Calendar.getInstance();
//		cal.setTimeInMillis(System.currentTimeMillis());
//		year = cal.get(Calendar.YEAR);
//		month = cal.get(Calendar.MONTH);
//		day = cal.get(Calendar.DAY_OF_MONTH);
//		datePicker.updateDate(year, month, day);
//		datePicker.updateDate(2011, 2, 3);
		return datePicker;
	}
	
	@Override
	protected void onBindDialogView(View view) {
		super.onBindDialogView(view);
		
		//datePicker.updateDate(year, month, day);
		datePicker.updateDate(year, month - 1, day);
	}
	
	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		// TODO Auto-generated method stub
		//return super.onGetDefaultValue(a, index);
		return a.getString(index);
	}
	
	@Override
	protected void onSetInitialValue(boolean restorePersistedValue,
			Object defaultValue) {
		String date = null;
		
		if (restorePersistedValue) {
//			if (defaultValue == null) {
//				// TODO
//				date = getPersistedString(getCurrentDate());
//			} else {
//				date = getPersistedString(defaultValue.toString());
//				Log.i(TAG, "onSetInitialValue:" + date);
//			}
			date = getPersistedString(getCurrentDate());
		} else {
			date = getCurrentDate();
			Log.i(TAG, "initial value:" + date);
		}
		year = getYear(date);
		month = getMonth(date);
		day = getDay(date);
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		// TODO Auto-generated method stub
		super.onDialogClosed(positiveResult);
		
		if (positiveResult) {
			year = datePicker.getYear();
			month = datePicker.getMonth() + 1;
			day = datePicker.getDayOfMonth();
			
			String date = year + "-" + month + "-" + day;
			Log.i(TAG, "before callchangelistener:" + date);
			
			if (callChangeListener(date)) {
				persistString(date);
			}
			Toast.makeText(getContext(), date, Toast.LENGTH_SHORT).show();
			Log.i(TAG, "close dialog: " + date);
		}
	}
	
	protected String getCurrentDate() {
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(System.currentTimeMillis());
		return cl.get(Calendar.YEAR) + "-" + (cl.get(Calendar.MONTH) + 1)   + "-" + cl.get(Calendar.DAY_OF_MONTH);
	}
}
