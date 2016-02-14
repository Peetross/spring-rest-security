package pl.peetross.rest.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.google.common.base.Optional;

import pl.peetross.rest.security.service.ExternalServiceAuthenticator;

/**
 *	@author <a href="mailto:peetross@gmail.com">Piotr Mszyca</a>
 */
public class UserRoleUsernamePasswordAuthenticationProvider implements AuthenticationProvider {
	
	private Logger log = LoggerFactory.getLogger(UserRoleUsernamePasswordAuthenticationProvider.class);
	
	private TokenService tokenService;
	private ExternalServiceAuthenticator externalServiceAuthenticator;
	
	public UserRoleUsernamePasswordAuthenticationProvider(TokenService tokenService, ExternalServiceAuthenticator externalServiceAuthenticator){
		this.tokenService = tokenService;
		this.externalServiceAuthenticator = externalServiceAuthenticator;
	}

	
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Optional<String> username = (Optional) authentication.getPrincipal();
        Optional<String> password = (Optional) authentication.getCredentials();

        log.debug("Checking authenticate:");
        if (!username.isPresent() || !password.isPresent()) {
            throw new BadCredentialsException("Invalid Domain User Credentials");
        }

        AuthenticationWithToken resultOfAuthentication = externalServiceAuthenticator.authenticate(username.get(), password.get());
        String newToken = tokenService.generateNewToken();
        resultOfAuthentication.setToken(newToken);
        tokenService.store(newToken, resultOfAuthentication);

        return resultOfAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
    	log.debug("Checking authenticate-support:");
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
