package eu.signme.app;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import eu.signme.app.api.SignMeAPI;
import eu.signme.app.api.SignMeAPI.LoginHandler;
import eu.signme.app.api.response.ErrorResponse;
import eu.signme.app.api.response.LoginResponse;
import eu.signme.app.util.Utils;

public class LoginActivity extends Activity implements OnClickListener {

	private Button btnLogin, btnRegister;
	private EditText inputEmail, inputPassword;
	private TextView txtError;

	Timer timer;
	TimerTask timerTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		bindViews();

	}

	private void bindViews() {
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnRegister = (Button) findViewById(R.id.btn_register);
		inputEmail = (EditText) findViewById(R.id.input_email);
		inputPassword = (EditText) findViewById(R.id.input_password);
		txtError = (TextView) findViewById(R.id.txt_error);

		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:

			startLoadingAnimation();

			final String email = inputEmail.getText().toString();
			final String password = inputPassword.getText().toString();

			if (Utils.isValidEmail(email))
				if (password.length() > 0)
				SignMeAPI.login(email, password, new LoginHandler() {

					@Override
					public void onSuccess(LoginResponse response) {
						stopLoadingAnimation();
						Intent intent = new Intent(LoginActivity.this,
								LecturesActivity.class);
						intent.putExtra("email", email);
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
							ErrorResponse errorObject = new Gson().fromJson(
									json, ErrorResponse.class);

							int stringResource = getResources().getIdentifier(
									"error_"
											+ Integer.toString(errorObject
													.getStatus()), "string",
									getPackageName());
							txtError.setText(stringResource);

						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
			else {
				stopLoadingAnimation();
				txtError.setText(getString(R.string.no_password));
			} else {
				stopLoadingAnimation();
				txtError.setText(getString(R.string.email_invalid));
			}
			break;
		case R.id.btn_register:
			Intent intent = new Intent(this, RegistrationActivity.class);
			startActivity(intent);
			break;
		}
	}


	private void setLoadingText(final CharSequence text) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				btnLogin.setText(text);
			}
		});
	}

	public void startLoadingAnimation() {
		// set a new Timer
		timer = new Timer();

		// initialize the TimerTask's job
		initializeLoadingTimerTask();
		timer.schedule(timerTask, 0, 150); //
	}

	public void stopLoadingAnimation() {
		
		// stop the timer, if it's not already null
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		setLoadingText("log in");
	}

	public void initializeLoadingTimerTask() {

		timerTask = new TimerTask() {
			public void run() {
				if (btnLogin.getText() == "loggin") {
					setLoadingText("logging");
				} else if (btnLogin.getText() == "logging") {
					setLoadingText("logging i");
				} else if (btnLogin.getText() == "logging i") {
					setLoadingText("logging in");
				} else if (btnLogin.getText() == "logging in") {
					setLoadingText("logging in.");
				} else if (btnLogin.getText() == "logging in.") {
					setLoadingText("logging in..");
				} else if (btnLogin.getText() == "logging in..") {
					setLoadingText("logging in...");
				} else {
					setLoadingText("loggin");
				}
			}
		};
	}

}
