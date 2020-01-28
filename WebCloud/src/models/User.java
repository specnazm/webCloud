package models;

public class User {
	private String email;
	private String password;
	private String name;
	private String surname;
	private String org;
	private Roles role;
	
	public User(String email, String password, String name, String surname, String org, Roles role) {
		super();
		this.email = email;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.org = org;
		this.role = role;
	}
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean verify(String email, String password) {
		if (this.email.equals(email) && this.password.equals(password))
			return true;
		else
			return false;
	}
	
}
