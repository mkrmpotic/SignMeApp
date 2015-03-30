package eu.signme.app.api.response;

public class LoginResponse {
	private String token;
	private String name;
	private int id;
	private int beers;

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBeers() {
		return this.beers;
	}

	public void setBeers(int beers) {
		this.beers = beers;
	}
}
