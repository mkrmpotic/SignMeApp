package eu.signme.app.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import eu.signme.app.R;
import eu.signme.app.ui.swipe.OnItemClickListener;

public class LecturesRowHolder extends RecyclerView.ViewHolder implements
		OnClickListener {
	public TextView txtName, txtDay, txtCount;
	public View view;
	private int position;
	private OnItemClickListener mListener;

	public LecturesRowHolder(View view, OnItemClickListener listener) {
		super(view);

		this.view = (View) view;
		this.txtCount = (TextView) view.findViewById(R.id.txt_count);
		this.txtName = (TextView) view.findViewById(R.id.txt_name);
		this.txtDay = (TextView) view.findViewById(R.id.txt_day);
		this.mListener = listener;
		view.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		mListener.onItemClick(view, position);
	}

	public void setPosition(int i) {
		this.position = i;
	}
}
