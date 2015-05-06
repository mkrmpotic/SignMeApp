package eu.signme.app.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import eu.signme.app.R;
import eu.signme.app.R.color;
import eu.signme.app.R.drawable;

public class HourPicker extends LinearLayout implements OnClickListener {

	public interface HourPickerListener {
		void onHourClicked(String itemId);
	}

	HourPickerListener hourPickerListener = null;

	View view, selected;
	Button selectedButton;
	Context context;
	int startHour = 8;

	public HourPicker(Context context) {
		super(context);
		init(context);
	}

	public HourPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	protected void init(Context context) {
		this.context = context;
		view = inflate(context, R.layout.element_hour_picker, null);
		selected = (Button) view.findViewById(R.id.btn_8);
		selectedButton = (Button) selected;
		selected.setSelected(true);

		view.findViewById(R.id.btn_8).setOnClickListener(this);
		view.findViewById(R.id.btn_9).setOnClickListener(this);
		view.findViewById(R.id.btn_10).setOnClickListener(this);
		view.findViewById(R.id.btn_11).setOnClickListener(this);
		view.findViewById(R.id.btn_12).setOnClickListener(this);
		view.findViewById(R.id.btn_13).setOnClickListener(this);
		view.findViewById(R.id.btn_14).setOnClickListener(this);
		view.findViewById(R.id.btn_15).setOnClickListener(this);
		view.findViewById(R.id.btn_16).setOnClickListener(this);
		view.findViewById(R.id.btn_17).setOnClickListener(this);
		view.findViewById(R.id.btn_18).setOnClickListener(this);
		view.findViewById(R.id.btn_19).setOnClickListener(this);
		view.findViewById(R.id.btn_20).setOnClickListener(this);
		view.findViewById(R.id.btn_21).setOnClickListener(this);

		addView(view);
	}

	@Override
	public void onClick(View v) {
		selected.setSelected(false);
		v.setSelected(true);
		selected = v;
		selectedButton = (Button) selected;
		hourPickerListener.onHourClicked(selectedButton.getText().toString());
	}

	public void setHourPickerListener(HourPickerListener listener) {
		hourPickerListener = listener;
	}

	public void adaptHour(String hour) {
		int stringResource;
		Button currentButton;

		int newStartHour = Integer.parseInt(hour);
		if (newStartHour < startHour) {
			for (int i = newStartHour + 1; i <= startHour; i++) {
				stringResource = getResources().getIdentifier("btn_" + i, "id",
						context.getPackageName());

				currentButton = (Button) view.findViewById(stringResource);
				currentButton
						.setBackgroundResource(R.drawable.btn_daypicker_background);
				currentButton.setClickable(true);
			}
		} else {
			for (int i = startHour; i <= newStartHour; i++) {
				stringResource = getResources().getIdentifier("btn_" + i, "id",
						context.getPackageName());

				currentButton = (Button) view.findViewById(stringResource);
				currentButton
						.setBackgroundResource(R.drawable.btn_unselectable_background);
				currentButton.setClickable(false);
			}
		}

		startHour = newStartHour;
		String nextHour = Integer.toString(startHour + 1);

		selected.setSelected(false);
		stringResource = getResources().getIdentifier("btn_" + nextHour, "id",
				context.getPackageName());
		currentButton = (Button) view.findViewById(stringResource);
		currentButton.setSelected(true);
		selected = currentButton;

	}

	public int getHourPosition(String hour) {
		int stringResource = getResources().getIdentifier("btn_" + hour, "id",
				context.getPackageName());
		return view.findViewById(stringResource).getLeft();
	}

	public void removeLastElement() {
		view.findViewById(R.id.btn_21).setVisibility(View.GONE);
	}

	public void focusOnSecondElement() {
		selected.setSelected(false);
		selected = (Button) view.findViewById(R.id.btn_9);
		selectedButton = (Button) selected;
		selected.setSelected(true);
	}

	public String getHour() {
		return selectedButton.getText().toString();
	}

}
