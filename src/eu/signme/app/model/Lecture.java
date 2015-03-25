package eu.signme.app.model;

import com.google.gson.annotations.SerializedName;

public class Lecture {
	private int id;
	private String name;
	private String date;

	@SerializedName("start_hour")
	private String startHour;

	@SerializedName("end_hour")
	private String endHour;
	
	@SerializedName("count_student_want_sign")
	private int signCount;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStartHour() {
		return this.startHour;
	}

	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}

	public String getEndHour() {
		return this.endHour;
	}

	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}
	
	public int getSignCount() {
		return this.signCount;
	}

	public void setSignCount(int signCount) {
		this.signCount = signCount;
	}
}
