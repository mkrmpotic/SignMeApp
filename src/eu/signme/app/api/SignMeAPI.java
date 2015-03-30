package eu.signme.app.api;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import eu.signme.app.api.request.GsonRequest;
import eu.signme.app.api.response.ChangeNameResponse;
import eu.signme.app.api.response.ChangePasswordResponse;
import eu.signme.app.api.response.CreateLectureResponse;
import eu.signme.app.api.response.GetLecturesResponse;
import eu.signme.app.api.response.GetSignaturesResponse;
import eu.signme.app.api.response.LoginResponse;
import eu.signme.app.api.response.RegisterGcmResponse;
import eu.signme.app.api.response.RegistrationResponse;
import eu.signme.app.api.response.RequestSignResponse;
import eu.signme.app.api.response.SignUserResponse;
import eu.signme.app.util.Utils;

public class SignMeAPI {

	public static final String BASE_URL = "http://oj.duckdns.org/";

	public static final String LOGIN = "login/";
	public static final String REGISTER = "signup/";
	public static final String REST = "rest/";
	public static final String LECTURES = "lectures/";
	public static final String REGISTER_GCM = "register/";
	public static final String LECTURE = "lecture/";
	public static final String CHANGE_NAME = "namechange/";
	public static final String CHANGE_PASS = "passchange/";
	public static final String SIGN = "swipe/";
	public static final String REQUEST_SIGN = "signrequest/";

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
			JSONObject obj, Listener<T> listener, ErrorListener errorListener) {
		VolleySingleton
				.getInstance()
				.getRequestQueue()
				.add(new GsonRequest<T>(Method.POST, getFullUrl(urlPart), obj,
						customClass, null, listener, errorListener));
	}

	private static <T> void sendGetWithToken(String urlPart,
			Class<T> customClass, Listener<T> listener,
			ErrorListener errorListener) {
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Authorization",
				"Token " + Utils.getStringFromPrefs("token"));
		VolleySingleton
				.getInstance()
				.getRequestQueue()
				.add(new GsonRequest<>(Method.GET, getFullUrl(urlPart), null,
						customClass, headerMap, listener, errorListener));
	}

	private static <T> void sendPostWithToken(String urlPart,
			Class<T> customClass, JSONObject obj, Listener<T> listener,
			ErrorListener errorListener) {
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Authorization",
				"Token " + Utils.getStringFromPrefs("token"));
		VolleySingleton
				.getInstance()
				.getRequestQueue()
				.add(new GsonRequest<>(Method.POST, getFullUrl(urlPart), obj,
						customClass, headerMap, listener, errorListener));
	}

	private static <T> void sendPutWithToken(String urlPart,
			Class<T> customClass, JSONObject obj, Listener<T> listener,
			ErrorListener errorListener) {
		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("Authorization",
				"Token " + Utils.getStringFromPrefs("token"));
		VolleySingleton
				.getInstance()
				.getRequestQueue()
				.add(new GsonRequest<>(Method.PUT, getFullUrl(urlPart), obj,
						customClass, headerMap, listener, errorListener));
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

		JSONObject obj = new JSONObject();

		try {
			obj.put("email", email);
			obj.put("password", password);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sendPost(LOGIN, LoginResponse.class, obj,
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

		JSONObject obj = new JSONObject();

		try {
			obj.put("email", email);
			obj.put("password", password);
			obj.put("name", name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sendPost(REGISTER, RegistrationResponse.class, obj,
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

	public static void createLecture(String name, String start, String end,
			boolean today, final CreateLectureHandler handler) {

		JSONObject obj = new JSONObject();

		try {
			obj.put("name", name);
			obj.put("start", Integer.parseInt(start));
			obj.put("end", Integer.parseInt(end));
			obj.put("today", today);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sendPostWithToken(REST + LECTURES, CreateLectureResponse.class, obj,
				new Listener<CreateLectureResponse>() {
					@Override
					public void onResponse(CreateLectureResponse response) {
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
	 * CreateLecture Response Handler
	 * 
	 * @author Marin
	 */
	public interface CreateLectureHandler {
		public void onSuccess(CreateLectureResponse response);

		public void onError(VolleyError error);
	}

	public static void registerGcm(String registrationId,
			final RegisterGcmHandler handler) {

		JSONObject obj = new JSONObject();

		try {
			obj.put("device_token", registrationId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sendPostWithToken(REST + REGISTER_GCM, RegisterGcmResponse.class, obj,
				new Listener<RegisterGcmResponse>() {
					@Override
					public void onResponse(RegisterGcmResponse response) {
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
	 * RegisterGcmHandler Response Handler
	 * 
	 * @author Marin
	 */
	public interface RegisterGcmHandler {
		public void onSuccess(RegisterGcmResponse response);

		public void onError(VolleyError error);
	}

	public static void getSignatures(int lectureId,
			final GetSignaturesHandler handler) {
		sendGetWithToken(REST + LECTURE + lectureId,
				GetSignaturesResponse.class,
				new Listener<GetSignaturesResponse>() {
					@Override
					public void onResponse(GetSignaturesResponse response) {
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
	 * GetSignatures Response Handler
	 * 
	 * @author Marin
	 */
	public interface GetSignaturesHandler {
		public void onSuccess(GetSignaturesResponse response);

		public void onError(VolleyError error);
	}

	public static void changeName(String name, final ChangeNameHandler handler) {

		JSONObject obj = new JSONObject();

		try {
			obj.put("name", name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sendPostWithToken(REST + CHANGE_NAME, ChangeNameResponse.class, obj,
				new Listener<ChangeNameResponse>() {
					@Override
					public void onResponse(ChangeNameResponse response) {
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
	 * RegisterGcmHandler Response Handler
	 * 
	 * @author Marin
	 */
	public interface ChangeNameHandler {
		public void onSuccess(ChangeNameResponse response);

		public void onError(VolleyError error);
	}

	public static void changePassword(String currentPassword,
			String newPassword, final ChangePasswordHandler handler) {

		JSONObject obj = new JSONObject();

		try {
			obj.put("password", currentPassword);
			obj.put("new_password", newPassword);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sendPostWithToken(REST + CHANGE_PASS, ChangePasswordResponse.class,
				obj, new Listener<ChangePasswordResponse>() {
					@Override
					public void onResponse(ChangePasswordResponse response) {
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
	 * RegisterGcmHandler Response Handler
	 * 
	 * @author Marin
	 */
	public interface ChangePasswordHandler {
		public void onSuccess(ChangePasswordResponse response);

		public void onError(VolleyError error);
	}

	public static void signUser(int signatureId, final SignUserHandler handler) {

		JSONObject obj = new JSONObject();

		try {
			obj.put("id", signatureId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sendPutWithToken(REST + SIGN, SignUserResponse.class, obj,
				new Listener<SignUserResponse>() {
					@Override
					public void onResponse(SignUserResponse response) {
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
	 * RegisterGcmHandler Response Handler
	 * 
	 * @author Marin
	 */
	public interface SignUserHandler {
		public void onSuccess(SignUserResponse response);

		public void onError(VolleyError error);
	}

	public static void requestSign(int lectureId,
			final RequestSignHandler handler) {

		JSONObject obj = new JSONObject();

		try {
			obj.put("lecture_id", lectureId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sendPostWithToken(REST + REQUEST_SIGN, RequestSignResponse.class, obj,
				new Listener<RequestSignResponse>() {
					@Override
					public void onResponse(RequestSignResponse response) {
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
	 * RegisterGcmHandler Response Handler
	 * 
	 * @author Marin
	 */
	public interface RequestSignHandler {
		public void onSuccess(RequestSignResponse response);

		public void onError(VolleyError error);
	}
}
