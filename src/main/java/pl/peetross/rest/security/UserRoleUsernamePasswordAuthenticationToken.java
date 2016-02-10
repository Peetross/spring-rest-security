package pl.peetross.rest.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 *	@author <a href="mailto:peetross@gmail.com">Piotr Mszyca</a>
 */
public class UserRoleUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

	public UserRoleUsernamePasswordAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}

}
