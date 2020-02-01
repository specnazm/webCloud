package src.models;

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
	
	
	
	@Override
	public String toString() {
		return "User [email=" + email + ", password=" + password + ", name=" + name + ", surname=" + surname + ", org="
				+ org + ", role=" + role + "]";
	}



	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void updateUser(String old_name, String new_name)
	{
		if (this.getOrg().equals(old_name))
			this.setOrg(new_name);
	}
	
	public boolean verify(String email, String password) {
		if (this.email.equals(email) && this.password.equals(password))
			return true;
		else
			return false;
	}

	public boolean checkRequired() {
		if (this.email == null || this.password == null || this.name == null || this.surname == null || this.role == null || this.org == null )
			return false;
		return true;
	}

	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getSurname() {
		return surname;
	}



	public void setSurname(String surname) {
		this.surname = surname;
	}



	public String getOrg() {
		return org;
	}



	public void setOrg(String org) {
		this.org = org;
	}



	public Roles getRole() {
		return role;
	}



	public void setRole(Roles role) {
		this.role = role;
	}
	
	
}
