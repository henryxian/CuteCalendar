package com.henryxian;

import java.util.Calendar;

import com.actionbarsherlock.app.SherlockDialogFragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
public class TimePickerFragment extends SherlockDialogFragment 
						implements TimePickerDialog.OnTimeSetListener {
	public interface OnTimeChangedListener {
		public void onTimeChanged(int hour, int minute);
	}
	
	private OnTimeChangedListener onTimeChangedListener;
	
	
	final Calendar c = Calendar.getInstance();
	int hour = c.get(Calendar.HOUR);
	int minute = c.get(Calendar.MINUTE);
			
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		Calendar c = Calendar.getInstance();
//		hour = c.get(Calendar.HOUR);
//		minute = c.get(Calendar.MINUTE);
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
		onTimeChangedListener.onTimeChanged(hourOfDay, minute);
	}
	
}
