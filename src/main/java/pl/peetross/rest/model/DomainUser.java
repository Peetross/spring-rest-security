package pl.peetross.rest.model;
/**
 * @author <a href="mailto:peetross@gmail.com">Piotr Mszyca</a>
 *
 */
public class DomainUser {
	
	private String username;
    
	public DomainUser(String username) {
		this.username = username;
	}

	public String getUsername() {
	    return username;
	}

	@Override
	public String toString() {
		return username;
	}

}
