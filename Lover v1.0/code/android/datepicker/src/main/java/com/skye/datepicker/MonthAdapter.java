package com.skye.datepicker;

import com.skye.datepickerdata.Data;
import com.skye.datepickerdata.Day;
import com.skye.datepickerdata.Month;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MonthAdapter extends BaseAdapter {
	private Context context;
	private ListView listview;
	private Day selectedDay;
	private OnDateSelectChangedListener listener;

	private class ViewHolder {
		public TextView month;
		public DaiesView daies;
	}

	public MonthAdapter(Context context, ListView listview) {
		this.context = context;
		this.listview = listview;
	}

	public void setListener(OnDateSelectChangedListener listener) {
		this.listener = listener;
	}

	public Day getSelectedDay() {
		return selectedDay;
	}

	public void setSelectedDay(Day selectedDay) {
		this.selectedDay = selectedDay;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return Data.size();
	}

	@Override
	public Object getItem(int position) {
		return Data.getDaies(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_month, parent, false);
			vh = new ViewHolder();
			vh.month = (TextView) convertView.findViewById(R.id.month);
			vh.daies = (DaiesView) convertView.findViewById(R.id.daies);
			convertView.setTag(vh);
		} else
			vh = (ViewHolder) convertView.getTag();
		Month month = Data.getMonth(position);
		vh.month.setText(month.toString());
		vh.daies.setDaies(Data.getDaies(position, selectedDay));
		vh.daies.setListener(new DaiesView.OnItemClickListener() {

			@Override
			public void onItemClick(DaiesView daies, Day day) {
				if (day.check) {
					selectedDay = null;
					day.check = false;
					daies.setDay(day);
				} else {
					if (selectedDay != null) {
						selectedDay.check = false;
						if (selectedDay.monthPosition == day.monthPosition)
							daies.setDay(selectedDay);
						else {
							if (selectedDay.monthPosition >= listview.getFirstVisiblePosition()
									&& selectedDay.monthPosition <= listview.getLastVisiblePosition()) {
								View other = listview
										.getChildAt(selectedDay.monthPosition - listview.getFirstVisiblePosition());
								DaiesView dv = (DaiesView) other.findViewById(R.id.daies);
								dv.setDay(selectedDay);
							}
						}
					}
					selectedDay = day;
					day.check = true;
					daies.setDay(day);
				}
				if (listener != null)
					listener.onDateSelectChanged(selectedDay);
			}
		});
		return convertView;
	}

}
