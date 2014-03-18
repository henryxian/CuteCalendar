package com.henryxian;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

public class SchoolWeekCalFragment extends CaldroidFragment{
	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		// TODO Auto-generated method stub
		return new SchoolWeekAdapter(getActivity(), month, year, getCaldroidData(), extraData);
	}
}
