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
		
		before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
//AUTH		
		post("/api/auth/login" , (req, res) -> {
			Session ss = req.session(true);
			res.type("application/json");
			String payload = req.body();
			User u = g.fromJson(payload, User.class);
			Boolean authenticated = false;
			User logged_user = new User();
			if (regUsers.containsKey(u.getEmail()) | regUsers.get(u.getEmail()).verify(u.getEmail(), u.getPassword())) {
				authenticated = true;
				logged_user = regUsers.get(u.getEmail());
			}
			if (authenticated) {
				res.status(200);
				ss.attribute("user", logged_user);
				return g.toJson(logged_user);
			}else {
				res.status(404);
				res.body("Login failed. No such user.");
				return res;
			}		
			
		});
		
		get("/api/auth/logout" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			
			if(u != null) {
				ss.invalidate();
				res.status(200);
			}else {
				res.status(400);
				res.body("No user logged in.");
				return res;
			}			
			
			return res;
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
				res.body("Action failed, user lacks permission.");
				return res;
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
				res.body("No user logged in, can't perform action.");
				return res;
			}
			
			if(u.getRole() != Roles.SUPER_ADMIN) {
				res.status(403);
				res.body("Action failed, user lacks permission.");
				return res;
			}else {
				String payload = req.body();
				Organisation org = g.fromJson(payload, Organisation.class);
				if(org.checkRequired()) {
					if(regOrgs.containsKey(org.getName())) {
						res.status(400);
						res.body("Organisation with same name already exists.");
						return res;
					}else {
						res.status(200);
						regOrgs.put(org.getName(), org);
						return g.toJson(org);
					}					
				}else {
					res.status(400);
					res.body("Fields missing.");
					return res;
				}
			}
			
		});
		
		patch("/api/org/:org_id" , (req, res) -> {
			Session ss = req.session(true);
			User u = ss.attribute("user");
			res.type("application/json");
			String org_name = req.params("org_id");
			
			if(u == null) {
				res.status(400);
				res.body("No user logged in, can't perform action.");
				return res;
			}
			
			if(u.getRole() == Roles.USER) {
				res.status(403);
				res.body("Action failed, user lacks permission.");
				return res;
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
							res.body("Cannot change organisation name, such name already exists.");
							return res;
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
		
		
		
	}

}
