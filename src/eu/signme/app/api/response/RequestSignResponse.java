package eu.signme.app.api.response;

import com.google.gson.annotations.SerializedName;

public class RequestSignResponse {
	@SerializedName("sign_requested")
	boolean requested;

	public boolean getRequested() {
		return this.requested;
	}

	public void setRequested(boolean requested) {
		this.requested = requested;
	}

}
