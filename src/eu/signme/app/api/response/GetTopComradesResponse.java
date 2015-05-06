package eu.signme.app.api.response;

import java.util.ArrayList;

import eu.signme.app.model.Comrade;

public class GetTopComradesResponse {
	ArrayList<Comrade> comrades;

	public ArrayList<Comrade> getComrades() {
		return this.comrades;
	}

	public void setComrades(ArrayList<Comrade> comrades) {
		this.comrades = comrades;
	}

}
