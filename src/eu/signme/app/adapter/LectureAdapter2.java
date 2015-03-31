package eu.signme.app.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import eu.signme.app.R;
import eu.signme.app.model.Lecture;
import eu.signme.app.util.Utils;

public class LectureAdapter2 extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Lecture> lectures;

	public LectureAdapter2(Activity activity, List<Lecture> lectures) {
		this.activity = activity;
		this.lectures = lectures;
	}

	@Override
	public int getCount() {
		return lectures.size();
	}

	@Override
	public Object getItem(int location) {
		return lectures.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.lectures_item, parent,
					false);

		TextView txtName = (TextView) convertView
				.findViewById(R.id.txt_name);
		TextView txtDay = (TextView) convertView
				.findViewById(R.id.txt_day);
		TextView txtCount = (TextView) convertView
				.findViewById(R.id.txt_count);

		if (position % 2 == 0) {
			convertView.setBackgroundResource(R.drawable.listrow_dark_background);
		} else {
			convertView.setBackgroundResource(R.drawable.listrow_light_background);
		}

		// Getting lecture data from a row
		Lecture lecture = lectures.get(position);

		// Name of a lecture
		txtName.setText(lecture.getName());

		// Day of a lecture
		txtDay.setText(Utils.getRelativeDay(lecture.getDate()));
		
		// Number of requested signatures
		txtCount.setText(Integer.toString(lecture.getSignCount()));

		return convertView;
	}

}
