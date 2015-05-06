package eu.signme.app;

import android.content.Intent;

/**
 * Activity Class that all activities are going to extend
 */

import android.support.v4.app.FragmentActivity;
import eu.signme.app.util.NetworkUtil;

public abstract class SignMeActivity extends FragmentActivity {

	@Override
	protected void onResume() {
		super.onResume();
		if (NetworkUtil.getConnectivityStatus(this) == 0) {
			Intent intent = new Intent(this, NoConnectionActivity.class);
			startActivity(intent);
		}
	}

}
