package eu.signme.app.api.response;

import java.util.ArrayList;

import eu.signme.app.model.Lecture;

public class GetLecturesResponse {
	ArrayList<Lecture> lectures;

	public ArrayList<Lecture> getLectures() {
		return this.lectures;
	}

	public void setLectures(ArrayList<Lecture> lectures) {
		this.lectures = lectures;
	}

}
