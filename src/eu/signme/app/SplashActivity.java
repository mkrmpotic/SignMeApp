package eu.signme.app;

import android.content.Intent;
import android.os.Bundle;
import eu.signme.app.util.Utils;

public class SplashActivity extends SignMeActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		if (!Utils.getStringFromPrefs("token").equals("none")) {
			Intent intent = new Intent(SplashActivity.this,
					LecturesActivity.class);
			startActivity(intent);
			finish();
		} else {
			Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}

	}

}
