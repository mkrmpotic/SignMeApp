package eu.signme.app.util;

public class Utils {
	public final static boolean isValidEmail(CharSequence target) {
		if (target == null)
			return false;

		return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}
}
