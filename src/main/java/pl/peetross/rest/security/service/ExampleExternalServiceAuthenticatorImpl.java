package pl.peetross.rest.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;

import pl.peetross.rest.model.DomainUser;
import pl.peetross.rest.security.AuthenticationWithToken;

/**
 * @author <a href="mailto:peetross@gmail.com">Piotr Mszyca</a>
 *
 */
public class ExampleExternalServiceAuthenticatorImpl implements ExternalServiceAuthenticator{

	private Logger log = LoggerFactory.getLogger(ExampleExternalServiceAuthenticatorImpl.class);
	
	@Override
	public AuthenticationWithToken authenticate(String username, String password) {
		//To change for other external services
		AuthenticationWithToken authenticationWithToken = new AuthenticationWithToken(new DomainUser(username), null,  AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
		if(authenticationWithToken.getName() != null)log.debug("Check authenticate, name: " + authenticationWithToken.getName());
		if (authenticationWithToken.getDetails() != null)log.debug("Check authenticate, details: " + authenticationWithToken.getDetails());
		if(authenticationWithToken.getCredentials() != null)log.debug("Check authenticate, Credentials: " + authenticationWithToken.getCredentials().toString());
		if(authenticationWithToken.getAuthorities() != null)log.debug("Check authenticate, Authorities: " + authenticationWithToken.getAuthorities().toString());
		if(authenticationWithToken.getToken() != null)log.debug("Check authenticate, token: " + authenticationWithToken.getToken());
		return authenticationWithToken;
	}

}
