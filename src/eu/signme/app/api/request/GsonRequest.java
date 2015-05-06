package eu.signme.app.api.request;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GsonRequest<T> extends JsonRequest<T> {
	private final Gson gson = new Gson();
	private final Class<T> clazz;
	private final Map<String, String> headers;
	private final Map<String, String> params;
	private final Listener<T> listener;

	/**
	 * Make a GET request and return a parsed object from JSON.
	 * 
	 * @param url
	 *            URL of the request to make
	 * @param clazz
	 *            Relevant class object, for Gson's reflection
	 * @param headers
	 *            Map of request headers
	 */
	public GsonRequest(int method, String url, JSONObject jsonRequest,
			Class<T> clazz, Map<String, String> headers, Listener<T> listener,
			ErrorListener errorListener) {
		super(method, url, (jsonRequest == null) ? null : jsonRequest
				.toString(), listener, errorListener);
		this.clazz = clazz;
		this.headers = headers;
		this.listener = listener;
		this.params = null;
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return headers != null ? headers : super.getHeaders();
	}

	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return params;
	}

	@Override
	protected void deliverResponse(T response) {
		listener.onResponse(response);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {

		try {
			String json = new String(response.data, "UTF-8");
			return Response.success(gson.fromJson(json, clazz),
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		}
	}
}