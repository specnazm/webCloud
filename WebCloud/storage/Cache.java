package storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

import src.models.Organisation;
import src.models.User;

public class Cache {
	private static HashMap<String, User> users;
	private static HashMap<String, Organisation> orgs;
	private static Gson gson;
	
	public static void putOrg(String name, Organisation org)
	{
		Cache.orgs.put(name, org);
	}
	
	public static void putUser(String name, User u)
	{
		Cache.users.put(name, u);
		Cache.orgs.get(u.getOrg()).addUser(name, u);
	}
	
	public static void removeUser(User u)
	{
		Cache.users.remove(u.getEmail());
		Cache.orgs.get(u.getOrg()).getUsers().remove(u.getEmail());
	}
	
	public static void save() throws JsonIOException, IOException
	{	
		Cache.gson = new GsonBuilder().create();
		Writer w = new FileWriter("storage/users.json");
		Cache.gson.toJson(Cache.users,w);
		w.close();
		w = new FileWriter("storage/organisations.json");
		Cache.gson.toJson(Cache.orgs, w);
		w.close();
	}
	
	public static void load() throws IOException
	{
		Type type_user = new TypeToken<HashMap<String, User>>(){}.getType();
		Type type_org = new TypeToken<HashMap<String, Organisation>>(){}.getType();
		Cache.gson = new GsonBuilder().create();
		BufferedReader br = new BufferedReader(new FileReader("storage/users.json"));
		Cache.setUsers(Cache.gson.fromJson(br, type_user));
		br.close();
		br = new BufferedReader(new FileReader("storage/organisations.json"));
		Cache.setOrgs(Cache.gson.fromJson(br, type_org));
		br.close();
		
	}
	
	public static HashMap<String, User> getUsers() {
		return Cache.users;
	}
	public static void setUsers(HashMap<String, User> regUsers) {
		Cache.users = regUsers;
	}
	public static HashMap<String, Organisation> getOrgs() {
		return Cache.orgs;
	}
	public static void setOrgs(HashMap<String, Organisation> regOrgs) {
		Cache.orgs = regOrgs;
	}
	
	
}
