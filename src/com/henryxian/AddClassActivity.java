package com.henryxian;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

public class AddClassActivity extends SherlockActivity implements 
					android.widget.AdapterView.OnItemSelectedListener{
	private static final String TAG = AddClassActivity.class.getSimpleName();
	
	private ArrayAdapter<CharSequence> adapter;
	private Spinner spinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_class);
		
		 this.spinner = (Spinner)findViewById(R.id.addClass_spinner);
		 this.adapter = ArrayAdapter.createFromResource(
				this, 
				R.array.class_array, 
				android.R.layout.simple_spinner_item
				);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
