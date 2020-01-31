package src.rest;

//Imports
import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.patch;
import static spark.Spark.options;
import static spark.Spark.before;
import static spark.Spark.put;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import spark.Session;
import src.models.Organisation;
import src.models.Roles;
import src.models.User;
import storage.Cache;

public class SparkMain {
		
		private static Gson g = new Gson();
		
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
				return ("Login failed. No such user.");
			}		
			
		});
		
		get("/api/auth/logout" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			if(u != null) {
				ss.invalidate();
				res.status(200);
				return  ("User successfully logged out.");
			}else {
				res.status(400);
				return ("No user logged in.");
			}			
			
		});
		
//ORGS----------------------------------------------------------------------------------------------------------------------------------	
		
		get("/api/org" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			
			if(u == null) {
				res.status(400);
				res.body("No user logged in, can't perform action.");
				return res;
			}
			
			if(u.getRole() != Roles.SUPER_ADMIN) {
				res.status(403);
				return ("Action failed, user lacks permission.");
			}else {
				return g.toJson(Cache.getOrgs());
			}
			
		});
		
		post("/api/org" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			
			if(u == null) {
				res.status(400);
				return ("No user logged in, can't perform action.");
			}
			
			if(u.getRole() != Roles.SUPER_ADMIN) {
				res.status(403);
				return ("Action failed, user lacks permission.");
			}else {
				String payload = req.body();
				Organisation org = g.fromJson(payload, Organisation.class);
				if(org.checkRequired()) {
					if(Cache.getOrgs().containsKey(org.getName())) {
						res.status(400);
						return ("Organisation with same name already exists.");
					}else {
						res.status(200);
						Cache.putOrg(org.getName(), org);
						Cache.save();
						return g.toJson(org);
					}					
				}else {
					res.status(400);
					return ("Fields missing.");
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
				return("No such organisation.");
			}
			
			if(u == null) {
				res.status(400);
				return ("No user logged in, can't perform action.");
			}
			
			
			if(u.getRole() == Roles.USER) {
				res.status(403);
				return ("Action failed, user lacks permission.");
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
					if(org_name != org.getName())
					{
						if(Cache.getOrgs().containsKey(org.getName())) 
						{
							res.status(400);
							return ("Cannot change organisation name, such name already exists.");
						}else 
						{
							Cache.getOrgs().get(org_name).setName(org.getName());
							Cache.putOrg(org.getName(), Cache.getOrgs().get(org_name));
							Cache.getOrgs().remove(org_name);
							Cache.save();
						}
						
					}
				}
				
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
				res.body("No user logged in, can't perform action.");
				return res;
			}
			
			if(!Cache.getOrgs().containsKey(org_name)) {
				res.status(400);
				return ("No organisation with such name.");
			}
			
			if (u.getRole() == Roles.USER || u.getRole() == Roles.USER)
			{
				if(u.getOrg().equals(org_name))
				{
					res.status(200);
					return g.toJson(Cache.getOrgs().get(org_name));				
				}else
				{
					res.status(400);
					return ("User doesn't have permission to view organisation.");
				}
			}else {
				res.status(200);
				return g.toJson(Cache.getOrgs().get(org_name));
			}
				
			
		});
		
//USER----------------------------------------------------------------------------------------------------------------------------------
		
		
	}

}
