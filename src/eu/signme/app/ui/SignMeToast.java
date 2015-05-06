package eu.signme.app.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import eu.signme.app.R;
import eu.signme.app.util.Fonts;

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

		TextView txtMessage = (TextView) customToastRoot
				.findViewById(R.id.txt_message);
		TextView txtMinus = (TextView) customToastRoot
				.findViewById(R.id.txt_minus);
		txtMessage.setTypeface(Fonts.getTypeface(context, Fonts.ROBOTO_LIGHT));
		txtMinus.setTypeface(Fonts.getTypeface(context, Fonts.ROBOTO_BOLD));
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

		TextView txtMinus = (TextView) customToastRoot
				.findViewById(R.id.txt_minus);
		text.setTypeface(Fonts.getTypeface(context, Fonts.ROBOTO_LIGHT));
		txtMinus.setTypeface(Fonts.getTypeface(context, Fonts.ROBOTO_BOLD));
	}
}
