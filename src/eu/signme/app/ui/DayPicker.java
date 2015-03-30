package eu.signme.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import eu.signme.app.R;

public class DayPicker extends LinearLayout implements OnClickListener {

	public interface DayPickerListener {
		void onDayClicked(int itemId);
	}

	DayPickerListener dayPickerListener = null;

	View selected;

	public DayPicker(Context context) {
		super(context);
		init(context);
	}

	public DayPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	protected void init(Context context) {
		View view = inflate(context, R.layout.element_day_picker, null);
		selected = (Button) view.findViewById(R.id.btn_today);
		selected.setSelected(true);
		
		view.findViewById(R.id.btn_today).setOnClickListener(this);
		view.findViewById(R.id.btn_tomorrow).setOnClickListener(this);

		addView(view);
	}

	@Override
	public void onClick(View v) {
		selected.setSelected(false);
		v.setSelected(true);
		selected = v;
	}

	public void setDayPickerListener(DayPickerListener listener) {
		dayPickerListener = listener;
	}
	
	public boolean getDay() {
		if (selected.getId() == R.id.btn_today) {
			return true;
		} else {
			return false;
		}
	}

}
