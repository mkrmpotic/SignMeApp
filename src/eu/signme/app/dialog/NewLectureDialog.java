package eu.signme.app.dialog;

import android.animation.ObjectAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import eu.signme.app.R;
import eu.signme.app.ui.DayPicker;
import eu.signme.app.ui.HourPicker;
import eu.signme.app.ui.HourPicker.HourPickerListener;

public class NewLectureDialog extends DialogFragment implements
		OnClickListener {

	public interface NewLectureDialogListener {
		void onFinishCreateLecture(String name, String start, String end, boolean today);
	}
	
	NewLectureDialogListener newLectureListener = null;

	private EditText mEditText;
	private DayPicker dayPicker;
	private HourPicker startHourPicker, endHourPicker;
	private Button btnCreate;
	HorizontalScrollView hsvEndHours;

	public NewLectureDialog() {
		// Empty constructor required for DialogFragment
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_new_lecture, container);
		hsvEndHours = (HorizontalScrollView) view.findViewById(R.id.hsv_end_hours);
		mEditText = (EditText) view.findViewById(R.id.input_name);
		dayPicker = (DayPicker) view.findViewById(R.id.day_picker);
		startHourPicker = (HourPicker) view
				.findViewById(R.id.start_hour_picker);
		endHourPicker = (HourPicker) view.findViewById(R.id.end_hour_picker);
		btnCreate = (Button) view.findViewById(R.id.btn_create);
		btnCreate.setOnClickListener(this);
		
		startHourPicker.removeLastElement();
		endHourPicker.focusOnSecondElement();
		startHourPicker.setHourPickerListener(new HourPickerListener() {

			@Override
			public void onHourClicked(String itemId) {
				endHourPicker.adaptHour(itemId);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					ObjectAnimator animator = ObjectAnimator.ofInt(
							hsvEndHours, "scrollX",
							endHourPicker.getHourPosition(itemId));
					animator.start();
				} else {
					hsvEndHours.scrollTo(
							endHourPicker.getHourPosition(itemId), 0);
				}

			}
		});

		endHourPicker.setHourPickerListener(new HourPickerListener() {

			@Override
			public void onHourClicked(String itemId) {

			}
		});

		// getDialog().getWindow().clearFlags(LayoutParams.FLAG_DIM_BEHIND);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

		// Show soft keyboard automatically
		mEditText.requestFocus();
		getDialog().getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_VISIBLE);


		return view;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_create:
			newLectureListener.onFinishCreateLecture(mEditText.getText().toString(), startHourPicker.getHour(), endHourPicker.getHour(), dayPicker.getDay());
			break;
		}
	}
	
	public void setNewLectureDialogListener(NewLectureDialogListener listener) {
		newLectureListener = listener;
	}




}
