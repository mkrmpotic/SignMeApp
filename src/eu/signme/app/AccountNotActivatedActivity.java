package eu.signme.app;

import java.io.UnsupportedEncodingException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import eu.signme.app.api.SignMeAPI;
import eu.signme.app.api.SignMeAPI.LoginHandler;
import eu.signme.app.api.SignMeAPI.ResendEmailHandler;
import eu.signme.app.api.response.ErrorResponse;
import eu.signme.app.api.response.LoginResponse;
import eu.signme.app.api.response.ResendEmailResponse;
import eu.signme.app.util.Fonts;
import eu.signme.app.util.NetworkUtil;
import eu.signme.app.util.Utils;

public class AccountNotActivatedActivity extends SignMeActivity implements
		OnClickListener {

	private Button btnResend, btnReload;

	private String email, password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_not_activated);

		Intent i = getIntent();
		email = i.getExtras().getString("email");
		password = i.getExtras().getString("password");

		bindViews();
	}

	private void bindViews() {
		btnResend = (Button) findViewById(R.id.btn_resend);
		btnReload = (Button) findViewById(R.id.btn_reload);

		TextView txtTitle = (TextView) findViewById(R.id.txt_title);
		TextView txtSubtitle = (TextView) findViewById(R.id.txt_subtitle);

		txtTitle.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_BOLD));
		txtSubtitle.setTypeface(Fonts.getTypeface(this, Fonts.ROBOTO_LIGHT));

		btnResend.setOnClickListener(this);
		btnReload.setOnClickListener(this);
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
								AccountNotActivatedActivity.this,
								EmailSentActivity.class);
						intent.putExtra("email", email);
						intent.putExtra("password", password);
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
		case R.id.btn_reload:
			if (NetworkUtil.getConnectivityStatus(this) != 0)
				SignMeAPI.login(email, password, new LoginHandler() {

					@Override
					public void onSuccess(LoginResponse response) {
						Utils.saveToPrefs("token", response.getToken());
						Utils.saveToPrefs("name", response.getName());
						Utils.saveToPrefs("id", response.getId());
						Utils.saveToPrefs("beer", response.getBeers());
						Intent intent = new Intent(
								AccountNotActivatedActivity.this,
								AccountActivatedActivity.class);
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
