package eu.signme.app.api.response;

public class RegistrationResponse {
	private String name, email;
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name ;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email ;
	}
}
