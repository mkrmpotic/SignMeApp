package eu.signme.app.util;

import android.app.Application;
import android.content.Context;

public class SignMeApp extends Application {
	private static SignMeApp mInstance;
	private static Context mAppContext;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		this.setAppContext(getApplicationContext());
	}

	public static SignMeApp getInstance() {
		return mInstance;
	}

	public static Context getAppContext() {
		return mAppContext;
	}

	public void setAppContext(Context mAppContext) {
		this.mAppContext = mAppContext;
	}
}
