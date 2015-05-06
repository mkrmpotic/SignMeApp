package eu.signme.app.util;

import android.content.Context;
import android.graphics.Typeface;

public class Fonts {

	public static final String ROBOTO_THIN = "fonts/Roboto-Thin.ttf";
	public static final String ROBOTO_LIGHT = "fonts/Roboto-Light.ttf";
	public static final String ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf";
	public static final String ROBOTO_MEDIUM = "fonts/Roboto-Medium.ttf";
	public static final String ROBOTO_BOLD = "fonts/Roboto-Bold.ttf";

	public static Typeface getTypeface(Context context, String typeface) {
		return Typeface.createFromAsset(context.getAssets(), typeface);
	}

}
