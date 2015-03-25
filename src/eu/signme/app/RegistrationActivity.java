package eu.signme.app;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import eu.signme.app.api.SignMeAPI;
import eu.signme.app.api.SignMeAPI.LoginHandler;
import eu.signme.app.api.SignMeAPI.RegistrationHandler;
import eu.signme.app.api.response.ErrorResponse;
import eu.signme.app.api.response.LoginResponse;
import eu.signme.app.api.response.RegistrationResponse;
import eu.signme.app.util.Utils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrationActivity extends Activity implements OnClickListener {

	private Button btnRegister;
	private EditText inputName, inputEmail, inputPassword, inputPasswordAgain;
	private TextView txtError;

	Timer timer;
	TimerTask timerTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);

		bindViews();
	}

	private void bindViews() {
		btnRegister = (Button) findViewById(R.id.btn_register);
		inputName = (EditText) findViewById(R.id.input_name);
		inputEmail = (EditText) findViewById(R.id.input_email);
		inputPassword = (EditText) findViewById(R.id.input_password);
		inputPasswordAgain = (EditText) findViewById(R.id.input_password_again);
		txtError = (TextView) findViewById(R.id.txt_error);

		btnRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register:

			startLoadingAnimation();

			final String name = inputName.getText().toString();
			final String email = inputEmail.getText().toString();
			final String password = inputPassword.getText().toString();
			final String passwordAgain = inputPasswordAgain.getText().toString();

			if (name.length() > 0 && email.length() > 0
					&& password.length() > 0 && passwordAgain.length() > 0)
				if (Utils.isValidEmail(email))
					if (password.equals(passwordAgain))
						if (password.length() >= 6)
							SignMeAPI.register(email, password, name,
									new RegistrationHandler() {

										@Override
										public void onSuccess(
												RegistrationResponse response) {
											stopLoadingAnimation();

											Intent intent = new Intent(
													RegistrationActivity.this,
													EmailSentActivity.class);
											startActivity(intent);
											finish();
										}

										@Override
										public void onError(VolleyError error) {
											stopLoadingAnimation();

											try {
												String json = new String(
														error.networkResponse.data,
														HttpHeaderParser
																.parseCharset(error.networkResponse.headers));
												ErrorResponse errorObject = new Gson()
														.fromJson(
																json,
																ErrorResponse.class);
												int errorStatus = errorObject.getStatus();

												int stringResource = getResources()
														.getIdentifier(
																"error_"
																		+ Integer
																				.toString(errorStatus),
																"string",
																getPackageName());
												// if email is already sent to that address
												if (errorStatus == 51) {
													Intent intent = new Intent(RegistrationActivity.this,
															EmailAlreadySentActivity.class);
													intent.putExtra("email", email);
													intent.putExtra("password", password);
													startActivity(intent);
													finish();
												} else {
													txtError.setText(stringResource);
												}

											} catch (UnsupportedEncodingException e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

										}
									});
						else {
							stopLoadingAnimation();
							txtError.setText(getString(R.string.password_too_short));
						}
					else {
						stopLoadingAnimation();
						txtError.setText(getString(R.string.passwords_do_not_match));
					}
				else {
					stopLoadingAnimation();
					txtError.setText(getString(R.string.email_invalid));
				}
			else {
				stopLoadingAnimation();
				txtError.setText(getString(R.string.all_fields_mandatory));
			}

			break;
		}
	}

	private void setLoadingText(final CharSequence text) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				btnRegister.setText(text);
			}
		});
	}

	public void startLoadingAnimation() {
		// set a new Timer
		timer = new Timer();

		// initialize the TimerTask's job
		initializeLoadingTimerTask();
		timer.schedule(timerTask, 0, 200); //
	}

	public void stopLoadingAnimation() {

		// stop the timer, if it's not already null
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		setLoadingText("register");
	}

	public void initializeLoadingTimerTask() {

		timerTask = new TimerTask() {
			public void run() {
				if (btnRegister.getText() == ".") {
					setLoadingText("..");
				} else if (btnRegister.getText() == "..") {
					setLoadingText("...");
				} else {
					setLoadingText(".");
				}
			}
		};
	}
}
