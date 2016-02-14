package pl.peetross.rest.security.service;

import pl.peetross.rest.security.AuthenticationWithToken;

/**
 * @author <a href="mailto:peetross@gmail.com">Piotr Mszyca</a>
 *
 */
public interface ExternalServiceAuthenticator {
	//return authenitcate object with user and pass. After this set token
	AuthenticationWithToken authenticate(String username, String password);

	
}
