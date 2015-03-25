package eu.signme.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.SharedPreferences;

public class Utils {

	public static final String PREFS_NAME = "SignMeData";

	public final static boolean isValidEmail(CharSequence target) {
		if (target == null)
			return false;

		return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}

	/**
	 * Save a string in SharedPreferences
	 * 
	 * @param key
	 * @param value
	 * @author Marin
	 * 
	 */
	public static void saveToPrefs(String key, String value) {
		SharedPreferences settings = SignMeApp.getAppContext()
				.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);

		editor.commit();
	}

	/**
	 * Get a string from SharedPreferences
	 * 
	 * @param key
	 * @return value
	 * @author Marin
	 * 
	 */
	public static String getFromPrefs(String key) {
		SharedPreferences settings = SignMeApp.getAppContext()
				.getSharedPreferences(PREFS_NAME, 0);

		return settings.getString(key, "none");
	}

	/**
	 * Return today or tomorrow string based on a given date
	 * 
	 * @param dateString
	 * @return "today" OR "tomorrow"
	 * @author Marin
	 * 
	 */
	public static String getRelativeDay(String dateString) {
		Date date = null;
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
		try {
			date = format.parse(dateString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar todayCal = Calendar.getInstance(); // today

		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(date); // given date

		Calendar tommorowCal = Calendar.getInstance();
		tommorowCal.add(Calendar.DAY_OF_YEAR, +1); // tommorow

		if (todayCal.get(Calendar.YEAR) == dateCal.get(Calendar.YEAR)
				&& todayCal.get(Calendar.DAY_OF_YEAR) == dateCal
						.get(Calendar.DAY_OF_YEAR)) {
			return "today";
		} else if (tommorowCal.get(Calendar.YEAR) == dateCal.get(Calendar.YEAR)
				&& tommorowCal.get(Calendar.DAY_OF_YEAR) == dateCal
						.get(Calendar.DAY_OF_YEAR)) {
			return "tomorrow";
		} else {
			return "unknown";
		}
	}
}
