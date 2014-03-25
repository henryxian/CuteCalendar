package com.henryxian;

import java.util.Calendar;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import com.actionbarsherlock.app.SherlockDialogFragment;
public class TimePickerFragment extends SherlockDialogFragment 
						implements TimePickerDialog.OnTimeSetListener {
	public interface OnTimeChangedListener {
		public void onTimeChanged(int hour, int minute, int flag);
	}
	private static final String TAG = "TimePickerFragment";
	
	private OnTimeChangedListener onTimeChangedListener;
	
	final Calendar cal = Calendar.getInstance();
	private int hour = cal.get(Calendar.HOUR_OF_DAY);
	private int minute = cal.get(Calendar.MINUTE);
	private int flag;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		Calendar c = Calendar.getInstance();
//		hour = c.get(Calendar.HOUR);
//		minute = c.get(Calendar.MINUTE);

		flag = getArguments().getInt("flag");
		Log.d(TAG, "flag " + flag);
		return new TimePickerDialog(
				getActivity(), 
				this, 
				hour, 
				minute, 
				DateFormat.is24HourFormat(getActivity())
			);
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			onTimeChangedListener = (OnTimeChangedListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() +  
					" must implement this interface");
		}
	}
	
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		this.hour = hourOfDay;
		this.minute = minute;
		onTimeChangedListener.onTimeChanged(hourOfDay, minute, flag);
	}
	
}
