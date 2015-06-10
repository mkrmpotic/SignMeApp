package eu.signme.app.api.request;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class MultipartRequest<T> extends Request<T> {

	private static final String FILE_PART_NAME = "picture";

	private MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
	private final Response.Listener<T> mListener;
	private final File mImageFile;
	protected Map<String, String> headers;
	private final Gson gson = new Gson();
	private final Class<T> clazz;

	public MultipartRequest(String url, Class<T> clazz, ErrorListener errorListener,
			Listener<T> listener, File imageFile, Map<String, String> headers) {
		super(Method.POST, url, errorListener);

		mListener = listener;
		mImageFile = imageFile;
		this.headers = headers;
		this.clazz = clazz;
		buildMultipartEntity();
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {

		if (headers == null || headers.equals(Collections.emptyMap())) {
			headers = new HashMap<String, String>();
		}

		headers.put("Accept", "application/json");

		return headers;
	}

	private void buildMultipartEntity() {
		mBuilder.addBinaryBody(FILE_PART_NAME, mImageFile,
				ContentType.create("image/jpeg"), mImageFile.getName());
		mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		mBuilder.setLaxMode().setBoundary("xx")
				.setCharset(Charset.forName("UTF-8"));
	}

	@Override
	public String getBodyContentType() {
		String contentTypeHeader = mBuilder.build().getContentType().getValue();
		return contentTypeHeader;
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			mBuilder.build().writeTo(bos);
		} catch (IOException e) {
			VolleyLog
					.e("IOException writing to ByteArrayOutputStream bos, building the multipart request.");
		}

		return bos.toByteArray();
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

	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}
}
