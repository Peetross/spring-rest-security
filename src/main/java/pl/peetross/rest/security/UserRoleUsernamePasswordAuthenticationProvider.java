package pl.peetross.rest.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

import com.google.common.base.Optional;

/**
 *	@author <a href="mailto:peetross@gmail.com">Piotr Mszyca</a>
 */
public class UserRoleUsernamePasswordAuthenticationProvider implements AuthenticationProvider {
	
	private Logger log = LoggerFactory.getLogger(UserRoleUsernamePasswordAuthenticationProvider.class);
	
	public static final String INVALID_ROLE_USER_CREDENTIALS = "Invalid Role User Credentials";

	@Value("${role.user.username}")
    private String roleUserUsername;

    @Value("${role.user.password}")
    private String roleUserPassword;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		Optional<String> username = (Optional) authentication.getPrincipal();
		Optional<String> password = (Optional) authentication.getCredentials();
		
		log.debug("Check authenticate in UserRoleUsernamePasswordAuthenticationProvider");
		
		if (credentialsMissing(username, password) || credentialsInvalid(username, password)) {
			throw new BadCredentialsException(INVALID_ROLE_USER_CREDENTIALS);
		}

		return new UsernamePasswordAuthenticationToken(username.get(), null,
             AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
	}

	private boolean credentialsMissing(Optional<String> username, Optional<String> password) {
		return !username.isPresent() || !password.isPresent();
	}
	
	private boolean credentialsInvalid(Optional<String> username, Optional<String> password) {
	    return !isBackendAdmin(username.get()) || !password.get().equals(roleUserPassword);
	}
	
	private boolean isBackendAdmin(String username) {
	    return roleUserUsername.equals(username);
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		log.debug("Check in supports");
		return authentication.equals(UserRoleUsernamePasswordAuthenticationToken.class);
	}

}
