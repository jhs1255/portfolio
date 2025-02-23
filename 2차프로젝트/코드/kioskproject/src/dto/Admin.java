package dto;

public class Admin {
	private String id;
	private String password;
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getAdmin() {
		return this.id;
	}
	
	public String getPassword() {
		return this.password;
	}
}
