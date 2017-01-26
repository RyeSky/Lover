package com.skye.datepicker;

import com.skye.datepickerdata.Data;
import com.skye.datepickerdata.Day;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;

public class DataPicker extends LinearLayout {
	private ListView listview;
	private MonthAdapter adapter;

	public DataPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.VERTICAL);
		LayoutInflater.from(context).inflate(R.layout.datepicker, this, true);
		listview = (ListView) findViewById(R.id.listview);
		adapter = new MonthAdapter(context, listview);
		listview.setAdapter(adapter);
	}

	public void setDefaultSelection() {
		listview.setSelection(Data.getInitPosition());
	}

	public void setSelection(Day day) {
		listview.setSelection(day.monthPosition);
		adapter.setSelectedDay(day);
	}

	public void setListener(OnDateSelectChangedListener listener) {
		adapter.setListener(listener);
	}

	public Day getSelectedDay() {
		return adapter.getSelectedDay();
	}
}
