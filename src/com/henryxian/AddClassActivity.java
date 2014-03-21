package com.henryxian;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

public class AddClassActivity extends SherlockActivity implements 
					android.widget.AdapterView.OnItemSelectedListener{
	private static final String TAG = AddClassActivity.class.getSimpleName();
	
	private ArrayAdapter<CharSequence> adapterClassOrder;
	private ArrayAdapter<CharSequence> adapterweekDay;
	private ArrayAdapter<CharSequence> adapterReccur;
	
	private EditText editTextClassName;
	private Spinner spinnerClassOrder;
	private Spinner spinnerweekDay;
	private Spinner spinnerReccur;
	private Button buttonOk;
	
	private long startMillis;
	private long endMillis;
	
	private int classOrder;
	private int weekDay;
	private int reccur;
	
	// recurrence flags
	private static final int REC_WEEK = 0;
	private static final int REC_SEMESTER = 1;
	private static final int REC_YEAR = 2;
	
	// class flags
	private static final int FIRST_CLASS = 0;
	private static final int SECOND_CLASS = 1;
	private static final int THIRD_CLASS = 2;
	private static final int FOURTH_CLASS = 3;
	private static final int FIFTH_CLASS = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_class);
		
		this.editTextClassName = (EditText)findViewById(R.id.addClass_editText_className);
		
		// set up the class order spinner
		 this.spinnerClassOrder = (Spinner)findViewById(R.id.addClass_spinner_classOrder);
		 this.adapterClassOrder = ArrayAdapter.createFromResource(
				this, 
				R.array.class_array, 
				android.R.layout.simple_spinner_item
				);
		adapterClassOrder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerClassOrder.setAdapter(adapterClassOrder);
		spinnerClassOrder.setOnItemSelectedListener(this);
		
		// set up the week day spinner
		this.spinnerweekDay = (Spinner)findViewById(R.id.addClass_spinner_weekDay);
		this.adapterweekDay = ArrayAdapter.createFromResource(
				this, 
				R.array.weekday_array, 
				android.R.layout.simple_spinner_item
				);
		adapterweekDay.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerweekDay.setAdapter(adapterweekDay);
		spinnerweekDay.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				AddClassActivity.this.weekDay = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		// set up the reccurence spinner
		this.spinnerReccur = (Spinner)findViewById(R.id.addClass_spinner_reccur);
		this.adapterReccur = ArrayAdapter.createFromResource(
				this, 
				R.array.reccur_array, 
				android.R.layout.simple_spinner_item
				);
		adapterReccur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerReccur.setAdapter(adapterReccur);
		spinnerReccur.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				AddClassActivity.this.reccur = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		this.classOrder = position;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void confirmAddClass(View view) {
		String className = editTextClassName.getText().toString();
		Toast.makeText(this, 
				" ClassName " + className +
				" classOrder: " + classOrder + 
				" reccurence " + reccur + 
				" weekDay " + weekDay,
				Toast.LENGTH_SHORT).show();
		
	}
}
