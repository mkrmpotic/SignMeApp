package eu.signme.app;

import java.io.UnsupportedEncodingException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import eu.signme.app.api.SignMeAPI;
import eu.signme.app.api.SignMeAPI.LoginHandler;
import eu.signme.app.api.SignMeAPI.ResendEmailHandler;
import eu.signme.app.api.response.ErrorResponse;
import eu.signme.app.api.response.LoginResponse;
import eu.signme.app.api.response.ResendEmailResponse;
import eu.signme.app.util.NetworkUtil;
import eu.signme.app.util.Utils;

public class EmailAlreadySentActivity extends SignMeActivity implements
		OnClickListener {

	private Button btnResend;

	private String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_email_already_sent);

		Intent i = getIntent();
		email = i.getExtras().getString("email");

		bindViews();
	}

	private void bindViews() {
		btnResend = (Button) findViewById(R.id.btn_resend);

		btnResend.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_resend:
			if (NetworkUtil.getConnectivityStatus(this) != 0)
				SignMeAPI.resendEmail(email, new ResendEmailHandler() {

					@Override
					public void onSuccess(ResendEmailResponse response) {
						Intent intent = new Intent(
								EmailAlreadySentActivity.this,
								LoginActivity.class);
						startActivity(intent);
						finish();
					}

					@Override
					public void onError(VolleyError error) {
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

						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});
			break;
		}
	}
}
