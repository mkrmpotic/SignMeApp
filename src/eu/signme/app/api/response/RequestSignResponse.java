package eu.signme.app.api.response;

import com.google.gson.annotations.SerializedName;

public class RequestSignResponse {
	@SerializedName("sign_requested")
	boolean requested;
	int beers;

	public boolean getRequested() {
		return this.requested;
	}

	public void setRequested(boolean requested) {
		this.requested = requested;
	}

	public int getBeers() {
		return this.beers;
	}

	public void setBeers(int beers) {
		this.beers = beers;
	}

}
