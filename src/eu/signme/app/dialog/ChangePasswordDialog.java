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
import android.widget.TextView;
import eu.signme.app.R;

public class ChangePasswordDialog extends DialogFragment implements
		OnClickListener {

	public interface ChangePasswordListener {
		void onFinishChangePasswordDialog(String currentPassword,
				String newPassword);
	}

	ChangePasswordListener changePasswordListener = null;

	private EditText inputCurrentPass, inputNewPass, inputNewPassAgain;
	private Button btnSubmit;
	private TextView txtError;

	public ChangePasswordDialog() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.dialog_change_password, container);
		inputCurrentPass = (EditText) view
				.findViewById(R.id.input_current_password);
		inputNewPass = (EditText) view.findViewById(R.id.input_new_password);
		inputNewPassAgain = (EditText) view
				.findViewById(R.id.input_new_password_again);
		txtError = (TextView) view.findViewById(R.id.txt_error);
		btnSubmit = (Button) view.findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(this);

		getDialog().getWindow().clearFlags(LayoutParams.FLAG_DIM_BEHIND);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

		// Show soft keyboard automatically
		inputCurrentPass.requestFocus();


		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			String currentPassword = inputCurrentPass.getText().toString();
			String newPassword = inputNewPass.getText().toString();
			String newPasswordAgain = inputNewPassAgain.getText().toString();
			if (currentPassword.length() > 0 && newPassword.length() > 0
					&& newPasswordAgain.length() > 0)
				if (newPassword.equals(newPasswordAgain))
					if (newPassword.length() >= 6)
						changePasswordListener.onFinishChangePasswordDialog(
								currentPassword, newPassword);
					else {
						setChangePasswordError(R.string.password_too_short);
					}
				else {
					setChangePasswordError(R.string.passwords_do_not_match);
				}

			else {
				setChangePasswordError(R.string.all_fields_mandatory);
			}

			break;
		}
	}

	public void setChangePasswordListener(ChangePasswordListener listener) {
		changePasswordListener = listener;
	}

	public void setChangePasswordError(int errorTextResource) {
		txtError.setVisibility(View.VISIBLE);
		txtError.setText(getString(errorTextResource));
	}

}
