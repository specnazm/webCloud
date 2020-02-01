package src.models;

import java.util.HashMap;

public class Organisation {
	private String name;
	private String desc;
	private String logo_url;
	private HashMap<String, User> users;
	private HashMap<String, VM> rsrc;
	
	
	
	public Organisation() {
		super();
		this.users = new HashMap<String,User>();
		this.rsrc = new HashMap<String, VM>();
	}
	
	
	
	public Organisation(String name, String desc, String logo_url, HashMap<String, User> users,
			HashMap<String, VM> rsrc) {
		super();
		this.name = name;
		this.desc = desc;
		this.logo_url = logo_url;
		this.users = users;
		this.rsrc = rsrc;
	}
	
	public boolean checkRequired() {
		if (this.name == null || this.name.equals(""))
			return false;
		return true;
	}	
	
	public void updateUsers() {
		for (User u : this.users.values()) {
			u.setOrg(this.name);
		}
	}
	
	public void addRsrc(String name, VM resource) {
		this.rsrc.put(name, resource);
	}
	
	public void addUser(String name, User user) {
		this.users.put(name, user);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getLogo_url() {
		return logo_url;
	}
	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}
	public HashMap<String, User> getUsers() {
		return users;
	}
	public void setUsers(HashMap<String, User> users) {
		this.users = users;
	}
	public HashMap<String, VM> getRsrc() {
		return rsrc;
	}
	public void setRsrc(HashMap<String, VM> rsrc) {
		this.rsrc = rsrc;
	}
	
	
}
