package eu.signme.app.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import eu.signme.app.R;
import eu.signme.app.model.Lecture;
import eu.signme.app.ui.swipe.OnItemClickListener;
import eu.signme.app.util.Fonts;
import eu.signme.app.util.Utils;
import eu.signme.app.viewholder.LecturesRowHolder;

public class LectureAdapter extends RecyclerView.Adapter<LecturesRowHolder> {

	private List<Lecture> lectures;
	private Context mContext;
	private OnItemClickListener clickListener = null;

	public LectureAdapter(Context context, List<Lecture> lectures) {
		this.lectures = lectures;
		this.mContext = context;
	}

	@Override
	public LecturesRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext()).inflate(
				R.layout.lectures_item, viewGroup, false);
		LecturesRowHolder mh = new LecturesRowHolder(v, clickListener);

		return mh;
	}

	@Override
	public void onBindViewHolder(LecturesRowHolder feedListRowHolder, int i) {

		feedListRowHolder.setPosition(i);

		if (i % 2 == 0) {
			feedListRowHolder.view
					.setBackgroundResource(R.drawable.listrow_dark_background);
		} else {
			feedListRowHolder.view
					.setBackgroundResource(R.drawable.listrow_light_background);
		}

		TextView txtName = feedListRowHolder.txtName;
		TextView txtDay = feedListRowHolder.txtDay;
		TextView txtCount = feedListRowHolder.txtCount;
		txtName.setTypeface(Fonts.getTypeface(mContext, Fonts.ROBOTO_MEDIUM));
		txtDay.setTypeface(Fonts.getTypeface(mContext, Fonts.ROBOTO_LIGHT));
		txtCount.setTypeface(Fonts.getTypeface(mContext, Fonts.ROBOTO_THIN));

		Lecture lecture = lectures.get(i);

		txtName.setText(lecture.getName());

		if (Utils.getRelativeDay(lecture.getDate()).equals("today")) {
			txtDay.setTextColor(mContext.getResources().getColorStateList(
					R.drawable.listview_subtitle_text_color_red));
			txtDay.setText("today");
		} else {
			txtDay.setTextColor(mContext.getResources().getColorStateList(
					R.drawable.listview_subtitle_text_color_green));
			txtDay.setText("tomorrow");
		}

		txtCount.setText(Integer.toString(lecture.getSignCount()));

	}

	@Override
	public int getItemCount() {
		return (null != lectures ? lectures.size() : 0);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		this.clickListener = listener;
	}

	public Lecture getItem(int location) {
		return lectures.get(location);
	}

}