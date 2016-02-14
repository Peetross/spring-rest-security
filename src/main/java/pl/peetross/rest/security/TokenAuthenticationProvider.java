package pl.peetross.rest.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.google.common.base.Optional;

/**
 * @author <a href="mailto:peetross@gmail.com">Piotr Mszyca</a>
 *
 */
public class TokenAuthenticationProvider implements AuthenticationProvider{

	private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationProvider.class);
	
	private TokenService tokenService;

	public TokenAuthenticationProvider(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Optional<String> token = (Optional) authentication.getPrincipal();
		logger.debug("and resolving Authentication object for user:" + authentication.getName() + ",with token:" + token);
		logger.debug("and :" + authentication.getDetails() + ",with token:" + token);
	
		if (!token.isPresent() || token.get().isEmpty()) {
	    	throw new BadCredentialsException("Invalid token");
	    }
	    if (!tokenService.contains(token.get())) {
	        throw new BadCredentialsException("Invalid token or token expired");
	    }
	    Authentication auth = tokenService.retrieve(token.get());
	    return auth;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		logger.debug("Checking support:" + authentication.getName());
		
		return authentication.equals(PreAuthenticatedAuthenticationToken.class);
	}
	

}
