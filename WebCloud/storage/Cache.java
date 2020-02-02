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

import src.models.Disc;
import src.models.Organisation;
import src.models.User;
import src.models.VM;
import src.models.VMCategory;

public class Cache {
	private static HashMap<String, User> users;
	private static HashMap<String, Organisation> orgs;
	private static HashMap<String, VMCategory> categories;
	private static HashMap<String, VM> vms;
	private static HashMap<String, Disc> discs;
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
	
	public static void putCat(String name, VMCategory cat)
	{
		Cache.categories.put(name,  cat);
	}
	
	public static void removeCat(String name)
	{
		Cache.categories.remove(name);
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
		w = new FileWriter("storage/categories.json");
		Cache.gson.toJson(Cache.categories, w);
		w.close();
		w = new FileWriter("storage/vms.json");
		Cache.gson.toJson(Cache.vms, w);
		w.close();
		w = new FileWriter("storage/discs.json");
		Cache.gson.toJson(Cache.discs, w);
		w.close();
		
	}
	
	public static void load() throws IOException
	{
		Type type_user = new TypeToken<HashMap<String, User>>(){}.getType();
		Type type_org = new TypeToken<HashMap<String, Organisation>>(){}.getType();
		Type type_category = new TypeToken<HashMap<String, VMCategory>>(){}.getType();
		Type type_vm = new TypeToken<HashMap<String, VM>>(){}.getType();
		Type type_disc = new TypeToken<HashMap<String, Disc>>(){}.getType();
		
		Cache.gson = new GsonBuilder().create();
		BufferedReader br = new BufferedReader(new FileReader("storage/users.json"));
		Cache.setUsers(Cache.gson.fromJson(br, type_user));
		br.close();
		br = new BufferedReader(new FileReader("storage/organisations.json"));
		Cache.setOrgs(Cache.gson.fromJson(br, type_org));
		br.close();
		br = new BufferedReader(new FileReader("storage/categories.json"));
		Cache.setCategories(Cache.gson.fromJson(br, type_category));
		br.close();
		br = new BufferedReader(new FileReader("storage/vms.json"));
		Cache.setVms(Cache.gson.fromJson(br, type_vm));
		br.close();
		br = new BufferedReader(new FileReader("storage/discs.json"));
		Cache.setDiscs(Cache.gson.fromJson(br, type_disc));
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

	public static HashMap<String, VMCategory> getCategories() {
		return Cache.categories;
	}

	public static void setCategories(HashMap<String, VMCategory> categories) {
		Cache.categories = categories;
	}

	public static HashMap<String, VM> getVms() {
		return Cache.vms;
	}

	public static void setVms(HashMap<String, VM> vms) {
		Cache.vms = vms;
	}

	public static HashMap<String, Disc> getDiscs() {
		return discs;
	}

	public static void setDiscs(HashMap<String, Disc> discs) {
		Cache.discs = discs;
	}

	public static Gson getGson() {
		return gson;
	}

	public static void setGson(Gson gson) {
		Cache.gson = gson;
	}

	public static void putDisc(String name, Disc disc) {
		Cache.getVms().get(disc.getVm()).getDiscs().put(name, disc);
		Cache.getDiscs().put(name, disc);		
	}

	public static void removeDisc(String disc_name) {
		Cache.getVms().get(Cache.getDiscs().get(disc_name).getVm()).getDiscs().remove(disc_name);
		Cache.getDiscs().remove(disc_name);
		
	}
	
	
}
