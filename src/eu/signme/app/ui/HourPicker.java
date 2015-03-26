package eu.signme.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import eu.signme.app.R;

public class HourPicker extends LinearLayout implements OnClickListener {

	public interface HourPickerListener {
		void onHourClicked(int itemId);
	}

	HourPickerListener hourPickerListener = null;

	View selected;

	public HourPicker(Context context) {
		super(context);
		init(context);
	}

	public HourPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	protected void init(Context context) {
		View view = inflate(context, R.layout.element_hour_picker, null);
		selected = (Button) view.findViewById(R.id.btn_08);
		selected.setSelected(true);
		
		view.findViewById(R.id.btn_08).setOnClickListener(this);
		view.findViewById(R.id.btn_09).setOnClickListener(this);
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

		addView(view);
	}

	@Override
	public void onClick(View v) {
		selected.setSelected(false);
		v.setSelected(true);
		selected = v;
	}

	public void setHourPickerListener(HourPickerListener listener) {
		hourPickerListener = listener;
	}

}
