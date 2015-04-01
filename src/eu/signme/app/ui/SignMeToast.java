package eu.signme.app.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import eu.signme.app.R;

public class SignMeToast extends Toast {

	LayoutInflater inflater;
	View customToastRoot;

	public SignMeToast(Context context) {
		super(context);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		customToastRoot = inflater.inflate(R.layout.toast_sign_me, null);
		this.setView(customToastRoot);
		this.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);

	}

	public SignMeToast(Context context, String message, int duration) {
		super(context);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		customToastRoot = inflater.inflate(R.layout.toast_sign_me, null);
		TextView text = (TextView) customToastRoot
				.findViewById(R.id.txt_message);
		text.setText(message);
		this.setView(customToastRoot);
		this.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
		this.setDuration(duration);
	}

}
