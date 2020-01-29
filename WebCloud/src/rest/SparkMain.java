package src.rest;

//Imports
import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.options;
import static spark.Spark.before;

import java.util.ArrayList;

import com.google.gson.Gson;

import spark.Session;
import src.models.Roles;
import src.models.User;

public class SparkMain {
		
		private static Gson g = new Gson();
		private static ArrayList<User> regUsers;
		
	public static void main(String[] args) {
		port(8079);
		
		regUsers = new ArrayList<User>();
		User super_admin = new User("superadmin@admin", "pass" , "superadmin", "superadmin" ,"" , Roles.SUPER_ADMIN);
		regUsers.add(super_admin);
		
		
		options("/*",
		        (request, response) -> {
		        	System.out.println("OVde");
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
			for (User user : regUsers) {
				if(user.verify(u.getEmail(), u.getPassword())) {
					authenticated = true;
					logged_user = user;
				}
					
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
			String s = u.toString();
			System.out.println(s);
			
			if(u != null) {
				ss.invalidate();
				res.status(200);
			}else {
				res.status(400);
			}
			
			
			return res;
		});
		
		
	}

}
