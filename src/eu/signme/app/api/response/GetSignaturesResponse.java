package eu.signme.app.api.response;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

import eu.signme.app.model.Signature;

public class GetSignaturesResponse {
	@SerializedName("students")
	ArrayList<Signature> signatures;

	public ArrayList<Signature> getSignatures() {
		return this.signatures;
	}

	public void setSignatures(ArrayList<Signature> signatures) {
		this.signatures = signatures;
	}
}
