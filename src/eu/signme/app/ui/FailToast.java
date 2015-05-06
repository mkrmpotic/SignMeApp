package eu.signme.app.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import eu.signme.app.R;
import eu.signme.app.util.Fonts;

public class FailToast extends Toast {

	LayoutInflater inflater;
	View customToastRoot;

	public FailToast(Context context) {
		super(context);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		customToastRoot = inflater.inflate(R.layout.toast_fail, null);
		this.setView(customToastRoot);
		this.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);

		TextView txtMessage = (TextView) customToastRoot
				.findViewById(R.id.txt_message);
		txtMessage.setTypeface(Fonts.getTypeface(context, Fonts.ROBOTO_LIGHT));

	}

	public FailToast(Context context, String message, int duration) {
		super(context);
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		customToastRoot = inflater.inflate(R.layout.toast_fail, null);
		TextView text = (TextView) customToastRoot
				.findViewById(R.id.txt_message);
		text.setText(message);
		this.setView(customToastRoot);
		this.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
		this.setDuration(duration);

		text.setTypeface(Fonts.getTypeface(context, Fonts.ROBOTO_LIGHT));
	}

}
