package eu.signme.app.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import eu.signme.app.R;
import eu.signme.app.R.color;
import eu.signme.app.model.Lecture;
import eu.signme.app.model.Signature;
import eu.signme.app.util.Utils;

public class SignatureAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Signature> signatures;
	private int myId;

	public SignatureAdapter(Activity activity, List<Signature> signatures, int userId) {
		this.activity = activity;
		this.signatures = signatures;
		this.myId = userId;
	}

	@Override
	public int getCount() {
		return signatures.size();
	}

	@Override
	public Object getItem(int location) {
		return signatures.get(location);
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
			convertView = inflater.inflate(R.layout.signatures_item, parent,
					false);

		TextView txtCount = (TextView) convertView
				.findViewById(R.id.txt_count);
		TextView txtName = (TextView) convertView
				.findViewById(R.id.txt_name);

		if (position % 2 == 0) {
			convertView.setBackgroundColor(activity.getResources().getColor(
					R.color.gray_background));
		} else {
			convertView.setBackgroundColor(activity.getResources().getColor(
					R.color.light_gray_background));
		}
		

		// Getting signature data from a row
		Signature signature = signatures.get(position);
		

		
		if (signature.getIUserd() == myId) {
			txtName.setText("Me");
			txtName.setTextColor(activity.getResources().getColor(
					R.color.signme_green));
		} else {
			// Name of a user
			txtName.setText(signature.getUserName());
		}

		
		// Number of a signature
		txtCount.setText(Integer.toString(position + 1));

		return convertView;
	}

}
