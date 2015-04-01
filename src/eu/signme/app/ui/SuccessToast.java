package eu.signme.app.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import eu.signme.app.R;

public class SuccessToast extends Toast {

	LayoutInflater inflater;
	View customToastRoot;

	public SuccessToast(Context context) {
		super(context);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		customToastRoot = inflater.inflate(R.layout.toast_success, null);
		this.setView(customToastRoot);
		this.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);

	}

	public SuccessToast(Context context, String message, int duration) {
		super(context);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		customToastRoot = inflater.inflate(R.layout.toast_success, null);
		TextView text = (TextView) customToastRoot
				.findViewById(R.id.txt_message);
		text.setText(message);
		this.setView(customToastRoot);
		this.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
		this.setDuration(duration);
	}

}
