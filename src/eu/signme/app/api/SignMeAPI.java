package eu.signme.app.api;

import java.util.HashMap;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import eu.signme.app.api.request.GsonRequest;
import eu.signme.app.api.response.LoginResponse;
import eu.signme.app.api.response.RegistrationResponse;

public class SignMeAPI {

	public static final String BASE_URL = "http://oj.duckdns.org/";

	public static final String LOGIN = "login/";
	public static final String REGISTER = "signup/";

	private static String getFullUrl(String secondPart) {
		return BASE_URL + secondPart;
	}

	/**
	 * Sending POST request via Volley. Response is being parsed to a model via Gson.  
	 * 
	 * @param urlPart - a part of the URL that is being concatenated to the base URL
	 * @param customClass - model
	 * @param map - POST variables
	 * @param listener
	 * @param errorListener
	 * @author Marin
	 */
	private static <T> void sendPost(String urlPart, Class<T> customClass,
			HashMap<String, String> map, Listener<T> listener,
			ErrorListener errorListener) {
		VolleySingleton
				.getInstance()
				.getRequestQueue()
				.add(new GsonRequest<T>(Method.POST, getFullUrl(urlPart),
						customClass, map, listener, errorListener));
	}

	/**
	 * API method for login.
	 * 
	 * @param email
	 * @param password
	 * @param handler
	 * @author Marin
	 */
	public static void login(String email, String password,
			final LoginHandler handler) {
		HashMap<String, String> requestParams = new HashMap<String, String>();

		requestParams.put("email", email);
		requestParams.put("password", password);

		sendPost(LOGIN, LoginResponse.class, requestParams,
				new Listener<LoginResponse>() {
					@Override
					public void onResponse(LoginResponse response) {
						handler.onSuccess(response);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						handler.onError(error);
					}

				});
	}

	/**
	 * Login Response Handler
	 * 
	 * @author Marin
	 *
	 */
	public interface LoginHandler {
		public void onSuccess(LoginResponse response);

		public void onError(VolleyError error);
	}

	/**
	 * API method for registration.
	 * 
	 * @param email
	 * @param password
	 * @param name
	 * @param handler
	 * @author Marin
	 */
	public static void register(String email, String password, String name,
			final RegistrationHandler handler) {
		HashMap<String, String> requestParams = new HashMap<String, String>();

		requestParams.put("email", email);
		requestParams.put("password", password);
		requestParams.put("name", name);

		sendPost(REGISTER, RegistrationResponse.class, requestParams,
				new Listener<RegistrationResponse>() {
					@Override
					public void onResponse(RegistrationResponse response) {
						handler.onSuccess(response);
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						handler.onError(error);
					}

				});
	}

	/**
	 * Registration Response Handler
	 * 
	 * @author Marin
	 *
	 */
	public interface RegistrationHandler {
		public void onSuccess(RegistrationResponse response);

		public void onError(VolleyError error);
	}

}
