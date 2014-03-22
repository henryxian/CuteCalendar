package com.henryxian;

import java.util.Calendar;

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
	
	private Spinner spinnerStartWeek;
	private ArrayAdapter<CharSequence> adapterStartWeek;
	
	private Spinner spinnerEndWeek;
	private ArrayAdapter<CharSequence> adapterEndWeek;
	
	private long startMillis;
	private long endMillis;
	
	private int classOrder;
	private int weekDay;
	private int reccur;
	
	private int classStartHour;
	private int classStartMin;
	private int classEndHour;
	private int classEndMin;
	
	private int startWeek;
	private int endWeek;
	
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
	
	protected void setClass(int position) {
		switch(position) {
		case 0:
			setClassHourMin(8, 30, 10, 5);
			break;
		case 1:
			setClassHourMin(10, 25, 12, 0);
			break;
		case 2:
			setClassHourMin(13, 50, 15, 25);
			break;
		case 3:
			setClassHourMin(15, 45, 17, 20);
			break;
		case 4:
			setClassHourMin(18, 20, 21, 0);
			break;
		default:
			setClassHourMin(8, 30, 10, 5);
			break;
		}
	}
	
	protected void setClassHourMin(int classStartHour, 
			int classStartMin, int classEndHour, int classEndMin) {
		this.classStartHour = classStartHour;
		this.classStartMin = classStartMin;
		this.classEndHour = classEndHour;
		this.classEndMin = classEndMin;
	}
	
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
		spinnerClassOrder.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				setClass(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
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
		
		// Set up the start-week spinner
		spinnerStartWeek = (Spinner)findViewById(R.id.addClass_spinner_classStartWeek);
		adapterStartWeek = ArrayAdapter.createFromResource(
				this, 
				R.array.schoolweek_array, 
				android.R.layout.simple_spinner_item
			);
		adapterStartWeek.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerStartWeek.setAdapter(adapterStartWeek);
		spinnerStartWeek.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				AddClassActivity.this.startWeek = position + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//Set up the end-week spinner
		spinnerEndWeek = (Spinner)findViewById(R.id.addClass_spinner_classEndWeek);
		adapterEndWeek = ArrayAdapter.createFromResource(
				this, 
				R.array.schoolweek_array, 
				android.R.layout.simple_spinner_item
			);
		adapterEndWeek.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerEndWeek.setAdapter(adapterEndWeek);
		spinnerEndWeek.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				AddClassActivity.this.endWeek = position + 1;
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
