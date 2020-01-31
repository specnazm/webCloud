package src.rest;

//Imports
import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.patch;
import static spark.Spark.options;
import static spark.Spark.before;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;

import spark.Session;
import src.models.Organisation;
import src.models.Roles;
import src.models.User;

public class SparkMain {
		
		private static Gson g = new Gson();
		private static HashMap<String, User> regUsers;
		private static HashMap<String, Organisation> regOrgs;
		
	public static void main(String[] args) {
		port(8079);
		
		regOrgs = new HashMap<String, Organisation>();
		regUsers = new HashMap<String, User>();
		User super_admin = new User("superadmin@admin", "pass" , "superadmin", "superadmin" ,"" , Roles.SUPER_ADMIN);
		regUsers.put(super_admin.getEmail(), super_admin);
		Organisation org1 = new Organisation();
		Organisation org2 = new Organisation();
		org1.setName("Org1");
		org2.setName("Org2");
		org1.setDesc("Prva!");
		org2.setDesc("Druga!");
		org1.setLogo_url("https://cdn.shopify.com/s/files/1/0080/8372/products/tattly_snowflake_tea_leigh_00_300x300.png?v=1531514165");
		org2.setLogo_url("https://cdn.windowsreport.com/wp-content/uploads/2013/01/alarm-clock-app-windows-10-1.jpg");
		regOrgs.put(org1.getName(), org1);
		regOrgs.put(org2.getName(), org2);
		
		
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
//AUTH		
		post("/api/auth/login" , (req, res) -> {
			Session ss = req.session(true);
			res.type("application/json");
			String payload = req.body();
			User u = g.fromJson(payload, User.class);
			Boolean authenticated = false;
			User logged_user = new User();
			if (regUsers.containsKey(u.getEmail()) && regUsers.get(u.getEmail()).verify(u.getEmail(), u.getPassword())) {
				authenticated = true;
				logged_user = regUsers.get(u.getEmail());
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
		
//ORGS	
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
				return g.toJson(regOrgs);
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
					if(regOrgs.containsKey(org.getName())) {
						res.status(400);
						return ("Organisation with same name already exists.");
					}else {
						res.status(200);
						regOrgs.put(org.getName(), org);
						return g.toJson(org);
					}					
				}else {
					res.status(400);
					return ("Fields missing.");
				}
			}
			
		});
		
		patch("/api/org/:name" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			String org_name = req.params("name");
			
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
					regOrgs.get(org_name).setDesc(org.getDesc());
				}
				
				if(org.getLogo_url()!=null)
				{
					regOrgs.get(org_name).setLogo_url(org.getLogo_url());
				}
				
				if(org.getName() != null)
				{
					if(org_name != org.getName())
					{
						if(regOrgs.containsKey(org.getName())) 
						{
							res.status(400);
							return ("Cannot change organisation name, such name already exists.");
						}else 
						{
							regOrgs.get(org_name).setName(org.getName());
							regOrgs.put(org.getName(), regOrgs.get(org_name));
							regOrgs.remove(org_name);
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
			
			if(!regOrgs.containsKey(org_name)) {
				res.status(400);
				return ("No organisation with such name.");
			}
			
			if (u.getRole() == Roles.USER || u.getRole() == Roles.USER)
			{
				if(u.getOrg().equals(org_name))
				{
					res.status(200);
					return g.toJson(regOrgs.get(org_name));				
				}else
				{
					res.status(400);
					return ("User doesn't have permission to view organisation.");
				}
			}else {
				res.status(200);
				return g.toJson(regOrgs.get(org_name));
			}
				
			
		});
		
	}

}
