package rest;

//Imports
import static spark.Spark.port;
import static spark.Spark.get;
import static spark.Spark.post;

public class SparkMain {

	public static void main(String[] args) {
		port(8080);
		
		post("/api/auth/login" , (req, res) -> {
			return true;
		});
	}

}
