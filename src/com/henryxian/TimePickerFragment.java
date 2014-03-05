package com.henryxian;

import java.util.Calendar;

import com.actionbarsherlock.app.SherlockDialogFragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.app.Dialog;
import android.app.TimePickerDialog;
public class TimePickerFragment extends SherlockDialogFragment 
						implements TimePickerDialog.OnTimeSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		return new TimePickerDialog(
				getActivity(), 
				this, 
				hour, 
				minute, 
				DateFormat.is24HourFormat(getActivity())
			);
	}
	
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub

	}
	
}
