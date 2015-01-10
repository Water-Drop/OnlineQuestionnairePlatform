package test;
//It's just for testing!
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import util.MD5Helper;

	@Path("/HelloWorld")
public class HelloWorld {
	 
	 @GET
	 @Produces(MediaType.TEXT_PLAIN)
	 public String helloWorld(){
	  String ret = "Hello World!";
	  String input = "123456";
	  ret = MD5Helper.generateMD5(input);
	  return ret;
	}
}

