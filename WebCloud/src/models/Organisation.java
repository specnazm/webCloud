package models;

import java.util.HashMap;

public class Organisation {
	private String name;
	private String desc;
	private HashMap<String, User> users;
	private HashMap<String, Organisation> orgs;
}
