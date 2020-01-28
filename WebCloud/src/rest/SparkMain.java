package rest;

//Imports
import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.post;

import java.util.ArrayList;

import com.google.gson.Gson;

import models.Roles;
import models.User;

public class SparkMain {
		
		private static Gson g = new Gson();
		private static ArrayList<User> regUsers;
		
	public static void main(String[] args) {
		port(8080);
		
		regUsers = new ArrayList<User>();
		User super_admin = new User("superadmin@admin", "pass" , "superadmin", "superadmin" ,"" , Roles.SUPER_ADMIN);
		regUsers.add(super_admin);
		
		
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
				
				res.redirect("api/dashboard");
				return g.toJson(logged);
			}else {
				return false;
			}
			
			
		});
	}

}
