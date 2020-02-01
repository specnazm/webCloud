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
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;

import spark.Session;
import src.models.Organisation;
import src.models.Roles;
import src.models.User;
import storage.Cache;

public class SparkMain {
		
		private static Gson g = new Gson();
		private static JsonObject msg = new JsonObject();
		
	public static void main(String[] args) throws JsonIOException, IOException {
		port(8079);		
		
		
		Cache.load();
		
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
		
		
//		delete("/api/user/:id" , (req, res) -> {
//			Session ss = req.session(true);
//			User u = ss.attribute("user");
//			res.type("application/json");
//			String id = req.params("id");
//			
//			if(u == null) {
//				res.status(400);
//				msg.addProperty("msg", "No user logged in.");
//				return g.toJson(msg);
//			}
//			
//			if(u.getRole() == Roles.USER) {
//				res.status(403);
//				msg.addProperty("msg", "User lacks permission.");
//				return g.toJson(msg);
//			}else if(u.getRole() == Roles.ADMIN) 
//			{
//				
//			}else
//			{
//				
//			}
//			
//			
//		});
		
		
	}

}
