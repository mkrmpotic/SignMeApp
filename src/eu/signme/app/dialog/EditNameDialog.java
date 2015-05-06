package eu.signme.app.dialog;

import android.graphics.drawable.ColorDrawable;
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
import eu.signme.app.R;

public class EditNameDialog extends DialogFragment implements OnClickListener {

	public interface EditNameDialogListener {
		void onFinishEditName(String name);
	}

	EditNameDialogListener editNameListener = null;

	private EditText inputName;
	private Button btnSave;
	private String name;

	public EditNameDialog(String name) {
		this.name = name;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_edit_name, container);
		inputName = (EditText) view.findViewById(R.id.input_name);
		btnSave = (Button) view.findViewById(R.id.btn_save);
		btnSave.setOnClickListener(this);

		inputName.setText(name);

		getDialog().getWindow().clearFlags(LayoutParams.FLAG_DIM_BEHIND);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));


		inputName.requestFocus();

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save:
			editNameListener.onFinishEditName(inputName.getText().toString());
			break;
		}
	}

	public void setEditNameDialogListener(EditNameDialogListener listener) {
		editNameListener = listener;
	}

}
