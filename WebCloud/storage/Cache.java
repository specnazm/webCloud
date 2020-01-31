package storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import src.models.Organisation;
import src.models.User;

public class Cache {
	private static HashMap<String, User> regUsers;
	private static HashMap<String, Organisation> regOrgs;
	private static Gson gson;
	
	public static void putOrg(String name, Organisation org)
	{
		Cache.regOrgs.put(name, org);
	}
	
	public static void putUser(String name, User u)
	{
		Cache.regUsers.put(name, u);
	}
	
	public static void save() throws JsonIOException, IOException
	{	
		Cache.gson = new GsonBuilder().create();
		Writer w = new FileWriter("storage/users.json");
		Cache.gson.toJson(Cache.regUsers,w);
		w.close();
		w = new FileWriter("storage/organisations.json");
		Cache.gson.toJson(Cache.regOrgs, w);
		w.close();
	}
	
	public static void load()
	{
		
	}
	
	public static HashMap<String, User> getRegUsers() {
		return regUsers;
	}
	public static void setRegUsers(HashMap<String, User> regUsers) {
		Cache.regUsers = regUsers;
	}
	public static HashMap<String, Organisation> getRegOrgs() {
		return regOrgs;
	}
	public static void setRegOrgs(HashMap<String, Organisation> regOrgs) {
		Cache.regOrgs = regOrgs;
	}
	
	
}
