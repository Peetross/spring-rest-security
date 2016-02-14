package pl.peetross.rest.api;
/**
 *	@author <a href="mailto:peetross@gmail.com">Piotr Mszyca</a>
 */
public abstract class ApiController {
	
	public static final String AUTH_URL = "/activate";
	
	public static final String GREETING_URL = "/greeting";
	
	public static final String HELLO_URL = "/hello";

	public String cos(){
		return "";
	}
}

