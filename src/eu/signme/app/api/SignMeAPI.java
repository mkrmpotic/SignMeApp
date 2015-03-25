package eu.signme.app.api;

import java.util.HashMap;

import android.util.Log;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import eu.signme.app.api.request.GsonRequest;
import eu.signme.app.api.response.GetLecturesResponse;
import eu.signme.app.api.response.LoginResponse;
import eu.signme.app.api.response.RegistrationResponse;
import eu.signme.app.util.Utils;

public class SignMeAPI {

	public static final String BASE_URL = "http://oj.duckdns.org/";

	public static final String LOGIN = "login/";
	public static final String REGISTER = "signup/";
	public static final String REST = "rest/";
	public static final String LECTURES = "lectures/";

	private static String getFullUrl(String secondPart) {
		return BASE_URL + secondPart;
	}

	/**
	 * Sending POST request via Volley. Response is being parsed to a model via
	 * Gson.
	 * 
	 * @param urlPart
	 *            - a part of the URL that is being concatenated to the base URL
	 * @param customClass
	 *            - model
	 * @param map
	 *            - POST variables
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

	private static <T> void sendGetWithToken(String urlPart,
			Class<T> customClass, Listener<T> listener,
			ErrorListener errorListener) {
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Authorization", "Token " + Utils.getFromPrefs("token"));
		VolleySingleton
				.getInstance()
				.getRequestQueue()
				.add(new GsonRequest<>(getFullUrl(urlPart), customClass,
						headerMap, listener, errorListener));
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
	 */
	public interface RegistrationHandler {
		public void onSuccess(RegistrationResponse response);

		public void onError(VolleyError error);
	}

	public static void getLectures(final GetLecturesHandler handler) {
		sendGetWithToken(REST + LECTURES, GetLecturesResponse.class,
				new Listener<GetLecturesResponse>() {
					@Override
					public void onResponse(GetLecturesResponse response) {
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
	 * GetLectures Response Handler
	 * 
	 * @author Marin
	 */
	public interface GetLecturesHandler {
		public void onSuccess(GetLecturesResponse response);

		public void onError(VolleyError error);
	}
}
