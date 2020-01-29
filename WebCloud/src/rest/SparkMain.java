package src.rest;

//Imports
import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.options;
import static spark.Spark.before;

import java.util.ArrayList;

import com.google.gson.Gson;

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
		
		post("/api/auth/login" , (req, res) -> {
			req.session(true);
			res.type("application/json");
			String payload_email = req.queryParams("email");
			String payload_pass = req.queryParams("password");
			Boolean authenticated = false;
			User logged = new User();
			for (User user : regUsers) {
				if(user.verify(payload_email, payload_pass)) {
					authenticated = true;
					logged = user;
				}
					
			}		
			if (authenticated) {	
				return g.toJson(logged);
			}else {
				res.status(404);
				return res;
			}		
			
		});
	}

}
