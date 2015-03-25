package eu.signme.app.api.response;

public class LoginResponse {
	private String token;
	private String name;
	
	public String getToken() {
		return this.token;
	}
	
	public void setToken(String token) {
		this.token = token ;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name ;
	}
}
