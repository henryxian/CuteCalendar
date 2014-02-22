package com.henryxian;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

public class TimePickerPreference extends DialogPreference {
	private int lastHour = 0;
	private int lastMinute = 0;
	private TimePicker picker = null;
	
	public static int getHour(String time) {
		String[] pieces = time.split(":");
		return Integer.parseInt(pieces[0]);
	}
	
	public static int getMinute(String time) {
		String[] pieces = time.split(":");
		return Integer.parseInt(pieces[1]);
	}
	
	public TimePickerPreference(Context ctx, AttributeSet attrs) {
		super(ctx, attrs);
		
		setPositiveButtonText(R.string.dialog_positive_button);
		setNegativeButtonText(R.string.dialog_negative_button);
	}

	@Override
	protected void onBindDialogView(View view) {
		// TODO Auto-generated method stub
		super.onBindDialogView(view);
		
		picker.setCurrentHour(lastHour);
		picker.setCurrentMinute(lastMinute);
	}

	@Override
	protected View onCreateDialogView() {
		// TODO Auto-generated method stub
		picker = new TimePicker(getContext());
		return picker;
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		// TODO Auto-generated method stub
		super.onDialogClosed(positiveResult);
		
		if (positiveResult) {
			lastHour = picker.getCurrentHour();
			lastMinute = picker.getCurrentMinute();
			
			String time = String.valueOf(lastHour) + ":" + String.valueOf(lastMinute);
			
			if (callChangeListener(time)) {
				persistString(time);
			}
			Toast.makeText(getContext(), time, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		// TODO Auto-generated method stub
		return a.getString(index);
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue,
			Object defaultValue) {
		// TODO Auto-generated method stub
		String time = null;
		
		if (restorePersistedValue) {
			if (defaultValue == null) {
				time = getPersistedString("00:00");
			} else {
				time = getPersistedString(defaultValue.toString());
			}
		} else {
			time = defaultValue.toString();
		}
		
		lastHour = getHour(time);
		lastMinute = getMinute(time);
	}
	
	
}
