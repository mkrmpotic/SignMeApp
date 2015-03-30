package eu.signme.app.model;

import com.google.gson.annotations.SerializedName;

public class Signature {
	@SerializedName("user_id")
	private int userId;

	@SerializedName("user_name")
	private String userName;

	@SerializedName("user_status")
	private int status;

	@SerializedName("id")
	private int signatureId;
	
	public Signature(int userId) {
		this.userId = userId;
	}

	public int getIUserd() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getId() {
		return this.signatureId;
	}

	public void setId(int signatureId) {
		this.signatureId = signatureId;
	}

	@Override
	public boolean equals(Object object) {
		boolean isEqual = false;

		if (object != null && object instanceof Signature) {
			isEqual = (this.userId == ((Signature) object).userId);
		}

		return isEqual;
	}

	@Override
	public int hashCode() {
		return this.userId;
	}

}
