package src.rest;

//Imports
import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.patch;
import static spark.Spark.options;
import static spark.Spark.before;
import static spark.Spark.put;
import static spark.Spark.delete;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import spark.Session;
import src.models.Organisation;
import src.models.Roles;
import src.models.User;
import src.models.VM;
import src.models.VMCategory;
import src.models.Disc;
import storage.Cache;

public class SparkMain {
		
		private static Gson g = new Gson();
		private static JsonObject msg = new JsonObject();
		
	public static void main(String[] args) throws JsonIOException, IOException {
		port(8079);	
		Cache.load();
		System.out.println(LocalDate.now());
		options("/*",
		        (request, response) -> {
		            String accessControlRequestHeaders = request
		                    .headers("Access-Control-Request-Headers");
		            if (accessControlRequestHeaders != null) {
		                response.header("Access-Control-Allow-Headers",
		                        accessControlRequestHeaders);
		            }

		            String accessControlRequestMethod = request
		                    .headers("Access-Control-Request-Method");
		            if (accessControlRequestMethod != null) {
		                response.header("Access-Control-Allow-Methods",
		                        accessControlRequestMethod);
		            }
		               
		            return "OK";
		        });
		
		before((request, response) -> {
			response.header("Access-Control-Allow-Origin", "http://localhost:8080");
			response.header("Access-Control-Allow-Credentials", "true");
		});
		
//AUTH----------------------------------------------------------------------------------------------------------------------------------		
		
		post("/api/auth/login" , (req, res) -> {
			Session ss = req.session(true);
			res.type("application/json");
			String payload = req.body();
			User u = g.fromJson(payload, User.class);
			Boolean authenticated = false;
			User logged_user = new User();;
			if (Cache.getUsers().containsKey(u.getEmail()) && Cache.getUsers().get(u.getEmail()).verify(u.getEmail(), u.getPassword())) {
				authenticated = true;
				logged_user = Cache.getUsers().get(u.getEmail());
			}
			if (authenticated) {
				res.status(200);
				ss.attribute("user", logged_user);
				return g.toJson(logged_user);
			}else {
				res.status(404);
				msg.addProperty("msg", "Invalid credentials.");
				return g.toJson(msg);
			}		
			
		});
		
		get("/api/auth/logout" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			if(u != null) {
				ss.invalidate();
				res.status(200);
				msg.addProperty("msg", "Invalid credentials.");
				return  ("User successfully logged out.");
			}else {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}			
			
		});
		
//ORGS----------------------------------------------------------------------------------------------------------------------------------	
		
		get("/api/org" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() == Roles.USER) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission.");
				return g.toJson(msg);
			}else if(u.getRole() == Roles.ADMIN){
				HashMap<String, Organisation> org = new HashMap<String, Organisation>();
				org.put(u.getOrg(), Cache.getOrgs().get(u.getOrg()));
				return g.toJson(org);
			}else
			{				
				return g.toJson(Cache.getOrgs());
			}
			
		});
		
		post("/api/org" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() != Roles.SUPER_ADMIN) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission to add organisations.");
				return g.toJson(msg);
			}else {
				String payload = req.body();
				Organisation org = g.fromJson(payload, Organisation.class);
				if(org.checkRequired()) {
					if(Cache.getOrgs().containsKey(org.getName())) {
						res.status(400);
						msg.addProperty("msg", "Organisation with same name already exists.");
						return g.toJson(msg);
					}else {
						res.status(200);
						Cache.putOrg(org.getName(), org);
						Cache.save();
						return g.toJson(org);
					}					
				}else {
					res.status(400);
					msg.addProperty("msg", "Fields missing.");
					return g.toJson(msg);
				}
			}
			
		});
		
		put("/api/org/:name" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			String org_name = req.params("name");
			
			if(!Cache.getOrgs().containsKey(org_name))
			{
				res.status(400);
				msg.addProperty("msg", "Can't change name of non-existing organisation.");
				return g.toJson(msg);
			}
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			
			if(u.getRole() == Roles.USER) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission.");
				return g.toJson(msg);
			}else {
				String payload = req.body();
				Organisation org = g.fromJson(payload, Organisation.class);
				
				if(org.getDesc()!= null) 
				{
					Cache.getOrgs().get(org_name).setDesc(org.getDesc());
				}
				
				if(org.getLogo_url()!=null)
				{
					Cache.getOrgs().get(org_name).setLogo_url(org.getLogo_url());
				}
				
				if(org.getName() != null)
				{
					if(!org_name.equals(org.getName()))
					{
						if(Cache.getOrgs().containsKey(org.getName())) 
						{
							res.status(400);
							msg.addProperty("msg", "Cannot change organisation name, such name already exists.");
							return g.toJson(msg);
						}else 
						{
							Cache.getOrgs().get(org_name).setName(org.getName());
							Cache.putOrg(org.getName(), Cache.getOrgs().get(org_name));
							Cache.getOrgs().remove(org_name);
							for (User user: Cache.getUsers().values()) {
								user.updateUser(org_name, org.getName());
							}
						}
					}
				}
				Cache.save();
				return g.toJson(org);
			}
			
		});
		
		get("/api/org/:name" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			String org_name = req.params("name");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in, can't perform action.");
				return g.toJson(msg);
			}
			
			if(!Cache.getOrgs().containsKey(org_name)) {
				res.status(400);
				msg.addProperty("msg", "No organisation with such name.");
				return g.toJson(msg);
			}
			
			if (u.getRole() == Roles.USER || u.getRole() == Roles.ADMIN)
			{
				if(u.getOrg().equals(org_name))
				{
					res.status(200);
					return g.toJson(Cache.getOrgs().get(org_name));				
				}else
				{
					res.status(400);
					msg.addProperty("msg", "User doesn't have permission to view organisation.");
					return g.toJson(msg);
				}
			}else {
				res.status(200);
				return g.toJson(Cache.getOrgs().get(org_name));
			}
				
			
		});
		
//USER----------------------------------------------------------------------------------------------------------------------------------
		
		get("/api/user" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			ArrayList<User> users = new ArrayList<User>();
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() == Roles.USER) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission.");
				return g.toJson(msg);
			}else if(u.getRole() == Roles.ADMIN) 
			{
				String org_name = u.getOrg();
				
				for (User user : Cache.getUsers().values())
				{
					if(user.getOrg().equals(org_name) && !user.getEmail().equals(u.getEmail()))
						users.add(user);					
				}
				res.status(200);
				return g.toJson(users);
			}else
			{
				String org_name = u.getOrg();
				
				for (User user : Cache.getUsers().values())
				{
					if(!user.getEmail().equals(u.getEmail()))
						users.add(user);					
				}
				res.status(200);
				return g.toJson(users);
			}
			
			
		});
		
		get("/api/user/:email" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			String user_email = req.params("email");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in, can't perform action.");
				return g.toJson(msg);
			}
			
			if(!Cache.getUsers().containsKey(user_email)) {
				res.status(400);
				msg.addProperty("msg", "No user with such email.");
				return g.toJson(msg);
			}
			
			if (u.getRole() == Roles.USER)
			{
				res.status(403);
				msg.addProperty("msg", "User doesn't have permission to view users.");
				return g.toJson(msg);
				
			}else if (u.getRole() == Roles.SUPER_ADMIN)
			{
				res.status(200);
				return g.toJson(Cache.getUsers().get(user_email));
			}
			else
			{
				if (u.getOrg().equals(Cache.getUsers().get(user_email).getOrg()))
				{
					res.status(200);
					return g.toJson(Cache.getUsers().get(user_email));
				}
				else
				{
					res.status(403);
					msg.addProperty("msg", "Admin lacks permission to view requested user.");
					return g.toJson(msg);
				}
			}
				
			
		});
		
		
		post("/api/user" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() == Roles.USER) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission to add users.");
				return g.toJson(msg);
			}
			else
			{
				String payload = req.body();
				User user = g.fromJson(payload, User.class);
				
				if (u.getRole() == Roles.ADMIN)
					user.setOrg(u.getOrg());
				
				if(user.checkRequired()) {
					if(Cache.getUsers().containsKey(user.getEmail())) {
						res.status(400);
						msg.addProperty("msg", "User with such email already exists.");
						return g.toJson(msg);
					}else {
						res.status(200);
						Cache.putUser(user.getEmail(), user);
						Cache.save();
						return g.toJson(user);
					}					
				}else {
					res.status(400);
					msg.addProperty("msg", "Fields missing.");
					return g.toJson(msg);
				}
			}
			
		});
		
		put("/api/user/:email" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			String user_email = req.params("email");
			
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(!Cache.getUsers().containsKey(user_email)) {
				res.status(400);
				msg.addProperty("msg", "No user with such email exists.");
				return g.toJson(msg);
			}
			
			if(u.getRole() == Roles.USER) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission to edit users.");
				return g.toJson(msg);
			}
			else 
			{
				
				String payload = req.body();
				User payload_user = g.fromJson(payload, User.class); 
				if (u.getRole() == Roles.ADMIN && (!u.getOrg().equals(payload_user.getOrg())))
				{
					res.status(403);
					msg.addProperty("msg", "Admin lacks permission to edit user from other organisations.");
					return g.toJson(msg);
				}
				
				User tmp = Cache.getUsers().get(user_email);
				tmp.setName(payload_user.getName());
				tmp.setPassword(payload_user.getPassword());
				tmp.setRole(payload_user.getRole());
				tmp.setSurname(payload_user.getSurname());
				Cache.removeUser(tmp);
				Cache.putUser(tmp.getEmail(), tmp);
				Cache.save();
				return g.toJson(tmp);
				
			}
			
		});
		
		put("/api/me" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
					
				
			String payload = req.body();
			User payload_user = g.fromJson(payload, User.class); 
			
			
			if (!u.getOrg().equals(payload_user.getOrg()) || !u.getRole().equals(payload_user.getRole()))
			{
				res.status(403);
				msg.addProperty("msg", "User lacks permission to edit own organisation or role.");
				return g.toJson(msg);
			}
			
			User tmp = new User();			
			tmp.setName(payload_user.getName());
			tmp.setPassword(payload_user.getPassword());
			tmp.setEmail(payload_user.getEmail());
			tmp.setSurname(payload_user.getSurname());
			tmp.setRole(u.getRole());
			tmp.setOrg(u.getOrg());
			
			if(Cache.getUsers().containsKey(tmp.getEmail()))
			{
				res.status(400);
				msg.addProperty("msg", "Can't change email, such email already exists.");
				return g.toJson(msg);
			}
			
			if(u.getRole() == Roles.SUPER_ADMIN) {
				Cache.getUsers().remove(u.getEmail());
				Cache.getUsers().put(tmp.getEmail(), tmp);
			}
			else
			{
				Cache.removeUser(u);
				Cache.putUser(tmp.getEmail(), tmp);
			}
						
			Cache.save();
			ss.attribute("user", tmp);
			return g.toJson(tmp);				
			
		});
		
		delete("/api/user/:email" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			String email = req.params("email");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(!Cache.getUsers().containsKey(email))
			{
				res.status(400);
				msg.addProperty("msg", "No such user.");
			}
			
			if(u.getRole() == Roles.USER) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission.");
				return g.toJson(msg);
			}
			else 
			{
				if(u.getEmail().equals(email)) {
					res.status(400);
					msg.addProperty("msg", "User can't delete self.");
					return msg;
				}
				
				if(!u.getOrg().equals(Cache.getUsers().get(email).getOrg()) && u.getRole()==Roles.ADMIN)
				{
					res.status(403);
					msg.addProperty("msg", "Admin not allowed to delete users from other organisations.");
					return g.toJson(msg);
				}
				
				Cache.removeUser(Cache.getUsers().get(email));
				Cache.save();
				res.status(201);
				return true;
			}
			
			
		});
		
//CATEGORIES----------------------------------------------------------------------------------------------------------------------------------
		
		get("/api/category" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() == Roles.USER) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission.");
				return g.toJson(msg);
			}else
			{
				return g.toJson(Cache.getCategories().values());
			}
			
		});
		
		post("/api/category" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() != Roles.SUPER_ADMIN) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission to add categories.");
				return g.toJson(msg);
			}else 
			{
				String payload = req.body();
				VMCategory cat = g.fromJson(payload, VMCategory.class);
				if(cat.checkRequired()) {
					if(Cache.getCategories().containsKey(cat.getName())) {
						res.status(400);
						msg.addProperty("msg", "Category with same name already exists.");
						return g.toJson(msg);
					}else {
						res.status(200);
						Cache.putCat(cat.getName(), cat);
						Cache.save();
						return g.toJson(cat);
					}					
				}else {
					res.status(400);
					msg.addProperty("msg", "Fields missing.");
					return g.toJson(msg);
				}
			}
			
		});
		
		delete("/api/category/:name" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			String cat_name = req.params("name");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() != Roles.SUPER_ADMIN) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission.");
				return g.toJson(msg);
			}else
			{
				
				for (VM vm : Cache.getVms().values())
				{
					if(vm.getCategory().equals(cat_name))
					{
						res.status(400);
						msg.addProperty("msg", "Can't delete category referenced in virtual machines");
						return g.toJson(msg);
					}
				}
				
				if(!Cache.getCategories().containsKey(cat_name))
				{
					res.status(400);
					msg.addProperty("msg", "Can't delete non-existing category");
					return g.toJson(msg);
				}
				
				Cache.getCategories().remove(cat_name);
				Cache.save();
				res.status(200);
				return true;
			}
			
		});
		
		get("/api/category/:name" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			String cat_name = req.params("name");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() != Roles.SUPER_ADMIN) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission.");
				return g.toJson(msg);
			}else
			{
				if(!Cache.getCategories().containsKey(cat_name)) 
				{
					res.status(400);
					msg.addProperty("msg", "No such category exists.");
					return g.toJson(msg);
				}
				res.status(200);
				return g.toJson(Cache.getCategories().get(cat_name));
			}
			
		}); 
		
		put("/api/category/:name" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			String cat_name = req.params("name");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() != Roles.SUPER_ADMIN) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission.");
				return g.toJson(msg);
			}else
			{
				if(!Cache.getCategories().containsKey(cat_name)) 
				{
					res.status(400);
					msg.addProperty("msg", "No such category exists.");
					return g.toJson(msg);
				}
				
				
				
				String payload = req.body();
				VMCategory cat = g.fromJson(payload, VMCategory.class);
				
				if(Cache.getCategories().containsKey(cat.getName()) && !(cat_name.equals(cat.getName()))) {
					res.status(400);
					msg.addProperty("msg", "Can't change category name, such category already exists.");
					return g.toJson(msg);
				}
				
				for (VM vm : Cache.getVms().values())
					vm.updateCat(cat_name, cat);
				
				Cache.removeCat(cat_name);
				Cache.putCat(cat.getName(), cat);
				Cache.save();
				res.status(200);
				return g.toJson(cat);
			}
			
		}); 
		
//DISC----------------------------------------------------------------------------------------------------------------------------------
	
		get("/api/disc" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			ArrayList<Disc> discs = new ArrayList<Disc>();
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() != Roles.SUPER_ADMIN) {
				String org_name = u.getOrg();
				
				if (req.queryParams().contains("org") && !org_name.equals(req.queryParams("org"))) {
					res.status(403);
					msg.addProperty("msg", "User lacks permission to view discs of other organisations.");
					return g.toJson(msg);
				}
				
				for (Disc d : Cache.getDiscs().values())
				{
					if(d.getOrg().equals(org_name))
						discs.add(d);
				}
				
				res.status(200);
				return g.toJson(discs);
			}else
			{
				if(req.queryParams().contains("org")) {
					String org_name = req.queryParams("org");
					for (Disc disc : Cache.getDiscs().values())
					{
						if(disc.getOrg().equals(org_name) && disc.getVm().equals(""))
							discs.add(disc);
					}
					res.status();
					return g.toJson(discs);
				}
				else
				{
					res.status(200);
					return g.toJson(Cache.getDiscs().values());
				}
			}
			
		});
		
		post("/api/disc" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() == Roles.USER) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission to add users.");
				return g.toJson(msg);
			}
			else
			{
				String payload = req.body();
				Disc disc = g.fromJson(payload, Disc.class);
				
				if (u.getRole() == Roles.ADMIN)
					disc.setOrg(u.getOrg());
				
				if(disc.checkRequired()) {
					if(Cache.getDiscs().containsKey(disc.getName())) {
						res.status(400);
						msg.addProperty("msg", "Disc with such name already exists.");
						return g.toJson(msg);
					}else {
						res.status(200);
						if (!(disc.getVm().equals("")))
							Cache.putDisc(disc.getName(), disc);
						else
							Cache.getDiscs().put(disc.getName(), disc);
						Cache.save();
						return g.toJson(disc);
					}					
				}else {
					res.status(400);
					msg.addProperty("msg", "Fields missing.");
					return g.toJson(msg);
				}
			}
		});
		
		delete("/api/disc/:name" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			String disc_name = req.params("name");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() != Roles.SUPER_ADMIN) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission to remove discs");
				return g.toJson(msg);
			}else
			{
				if(!Cache.getDiscs().containsKey(disc_name))
				{
					res.status(400);
					msg.addProperty("msg", "No disc with such name exists.");
					return g.toJson(msg);
				}
				
				if(Cache.getDiscs().get(disc_name).getVm() != "")
					Cache.removeDisc(disc_name);
				else
					Cache.getDiscs().remove(disc_name);
				Cache.save();
				res.status(200);
				return true;
			}
			
		});
		
		put("/api/disc/:name" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			String disc_name = req.params("name");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() == Roles.USER) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission to remove discs");
				return g.toJson(msg);
			}else
			{
				String payload = req.body();
				Disc disc = g.fromJson(payload, Disc.class);
				
				if(!disc.checkRequired())
				{
					res.status(400);
					msg.addProperty("msg", "Fields missing");
					return msg;
				}
				
				if(!disc.getOrg().equals(Cache.getDiscs().get(disc_name).getOrg()))
				{
					res.status(400);
					msg.addProperty("msg", "Can't change organisation of disc.");
					return msg;
				}
				
				if(u.getRole() == Roles.ADMIN && !(u.getOrg().equals(disc.getOrg())))
				{
					res.status(403);
					msg.addProperty("msg", "Admin can't change disc from different organisation.");
					return msg;
				}
				
				
				
				for (VM vm : Cache.getVms().values()) {
					vm.updateDisc(disc_name, disc);
				}
				
				if(!disc.getName().equals(disc_name)) {
					if(Cache.getDiscs().containsKey(disc.getName()))
					{
						res.status(400);
						msg.addProperty("msg", "Can't change disc name to existing disc name.");
						return msg;
					}
					Cache.getDiscs().remove(disc_name);
			}
				Cache.getDiscs().put(disc.getName(), disc);
				Cache.save();
				res.status(200);
				return g.toJson(disc);
			}
			
		});
		
		get("/api/disc/:name" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			String disc_name = req.params("name");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() == Roles.USER) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission.");
				return g.toJson(msg);
			}else
			{
				if(!Cache.getDiscs().containsKey(disc_name)) 
				{
					res.status(400);
					msg.addProperty("msg", "No such category exists.");
					return g.toJson(msg);
				}
				
				if(u.getRole()==Roles.ADMIN && !(u.getOrg().equals(Cache.getDiscs().get(disc_name).getOrg())))
				{
					res.status(403);
					msg.addProperty("msg", "Admin can't view discs of other organisations.");
					return msg;
				}
				
				res.status(200);
				return g.toJson(Cache.getDiscs().get(disc_name));
			}
			
		}); 
	
//VM----------------------------------------------------------------------------------------------------------------------------------	
		get("/api/vm" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			ArrayList<VM> vms = new ArrayList<VM>();
			
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() != Roles.SUPER_ADMIN) {
				String org_name = u.getOrg();
				if (req.queryParams().contains("org") && !org_name.equals(req.queryParams("org"))) {
					res.status(403);
					msg.addProperty("msg", "User lacks permission to view VMs of other organisations.");
					return g.toJson(msg);
				}
				
				for (VM vm : Cache.getVms().values())
				{
					if(vm.getOrg().equals(org_name))
						vms.add(vm);
				}
				res.status(200);
				return g.toJson(vms);
			}else
			{
				if(req.queryParams().contains("org")) {
					String org_name = req.queryParams("org");
					for (VM vm : Cache.getVms().values())
					{
						if(vm.getOrg().equals(org_name))
							vms.add(vm);
					}
					res.status();
					return g.toJson(vms);
				}
				else
				{
					res.status(200);
					return g.toJson(Cache.getVms().values());
				}
				
			}
			
		});
		
		get("/api/vm/:name" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			String vm_name = req.params("name");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in, can't perform action.");
				return g.toJson(msg);
			}
			
			if(!Cache.getVms().containsKey(vm_name)) {
				res.status(400);
				msg.addProperty("msg", "No VM with such name.");
				return g.toJson(msg);
			}
			
			if (u.getRole() == Roles.USER || u.getRole() == Roles.ADMIN)
			{
				if(u.getOrg().equals(Cache.getVms().get(vm_name).getOrg()))
				{
					res.status(200);
					return Cache.getVms().get(vm_name);				
				}else
				{
					res.status(400);
					msg.addProperty("msg", "User doesn't have permission to view VM.");
					return g.toJson(msg);
				}
			}else {
				res.status(200);
				return g.toJson(Cache.getVms().get(vm_name));
			}
				
			
		});
		
		post("/api/vm" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in, can't perform action.");
				return g.toJson(msg);
			}
			
			if(u.getRole() == Roles.USER) {
				res.status(403);
				msg.addProperty("msg", "User doesn't have permission to add VM.");
				return g.toJson(msg);
			}
			
			String payload = req.body();
			VM vm = g.fromJson(payload, VM.class);
			
			if(u.getRole() == Roles.ADMIN)
				vm.setOrg(u.getOrg());
			
			
			if(!vm.checkRequired()) {
				res.status(400);
				msg.addProperty("msg", "Fields missing.");
				return g.toJson(msg);
			}
			
			if(Cache.getVms().containsKey(vm.getName())) {
				res.status(400);
				msg.addProperty("msg", "Disc with such name already exists.");
				return g.toJson(msg);
			}else {
				for (Disc disc : vm.getDiscs().values()) {
					if(!disc.getOrg().equals(vm.getOrg()))
					{
						res.status(400);
						msg.addProperty("msg", "Discs in VM have to belong to same organisation as VM.");
						return g.toJson(msg);
					}					
				}
				
				Cache.putVM(vm);
				
				Cache.save();
				return g.toJson(vm);
			}
		});
		
		delete("/api/vm/:name" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			String vm_name = req.params("name");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() == Roles.USER) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission to remove discs");
				return g.toJson(msg);
			}else
			{
				if(!Cache.getVms().containsKey(vm_name))
				{
					res.status(400);
					msg.addProperty("msg", "No disc with such name exists.");
					return g.toJson(msg);
				}
				
				if(!Cache.getVms().get(vm_name).getDiscs().isEmpty())
				{
					for (String disc_name : Cache.getVms().get(vm_name).getDiscs().keySet()) {
						Cache.getDiscs().get(disc_name).setVm(null);
					}
				}
				Cache.getVms().remove(vm_name);
				Cache.save();
				res.status(200);
				return true;
			}
			
		});
		
		patch("/api/vm/toggle/:name" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			String vm_name = req.params("name");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() == Roles.USER) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission to change activity of VM.");
				return g.toJson(msg);
			}else
			{
				if(!Cache.getVms().containsKey(vm_name)) 
				{
					res.status(400);
					msg.addProperty("msg", "No such VM exists.");
					return g.toJson(msg);
				}
				
				if(u.getRole()==Roles.ADMIN && !(u.getOrg().equals(Cache.getVms().get(vm_name).getOrg())))
				{
					res.status(403);
					msg.addProperty("msg", "Admin can't change activity of VM in other organisation.");
					return msg;
				}
				
				res.status(200);
				Cache.getVms().get(vm_name).toggleActive();
				Cache.save();
				return g.toJson(Cache.getVms().get(vm_name));
			}
			
		}); 
		
		put("/api/vm/:name" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			String vm_name = req.params("name");
			
			if(u == null) {
				res.status(400);
				msg.addProperty("msg", "No user logged in.");
				return g.toJson(msg);
			}
			
			if(u.getRole() == Roles.USER) {
				res.status(403);
				msg.addProperty("msg", "User lacks permission to remove discs");
				return g.toJson(msg);
			}else
			{
				String payload = req.body();
				VM vm = g.fromJson(payload, VM.class);
				
				
				
				if(!vm.checkRequired())
				{
					res.status(400);
					msg.addProperty("msg", "Fields missing");
					return msg;
				}
				
				if(!vm.getOrg().equals(Cache.getVms().get(vm_name).getOrg()))
				{
					res.status(400);
					msg.addProperty("msg", "Can't change organisation of VM.");
					return msg;
				}
				
				if(u.getRole() == Roles.ADMIN && !(u.getOrg().equals(vm.getOrg())))
				{
					res.status(403);
					msg.addProperty("msg", "Admin can't change VM from different organisation.");
					return msg;
				}
								
				
				if(!vm.getName().equals(vm_name)) 
				{
					if(Cache.getVms().containsKey(vm.getName()))
					{
						res.status(400);
						msg.addProperty("msg", "Can't change disc name to existing disc name.");
						return msg;
					}
					for (Disc d : Cache.getVms().get(vm_name).getDiscs().values()) {
						d.updateVM(vm_name, vm.getName());
					}
					Cache.getVms().remove(vm_name);
				}	
				
				Cache.getVms().put(vm.getName(), vm);
				Cache.save();
				res.status(200);
				return g.toJson(vm);
			}
			
		});
		
	}

}
