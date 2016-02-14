package pl.peetross.rest.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

/**
 * @author <a href="mailto:peetross@gmail.com">Piotr Mszyca</a>
 *
 */
public class AuthenticationWithToken extends PreAuthenticatedAuthenticationToken{

	public AuthenticationWithToken(Object aPrincipal, Object aCredentials, Collection<? extends GrantedAuthority> anAuthorities) {
		super(aPrincipal, aCredentials, anAuthorities);

	}

	public AuthenticationWithToken(Object aPrincipal, Object aCredentials) {
		super(aPrincipal, aCredentials);

	}
	
	public void setToken(String token) {
		setDetails(token);
	}

	public String getToken() {
		return (String)getDetails();
	}
	
}
