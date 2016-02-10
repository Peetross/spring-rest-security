package pl.peetross.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

import pl.peetross.rest.api.ApiController;

/**
*	@author <a href="mailto:peetross@gmail.com">Piotr Mszyca</a>
*/

public class AuthenticationFilter extends GenericFilterBean{
	
	private Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
	
	public static final String TOKEN_SESSION_KEY = "token";
	public static final String USER_SESSION_KEY = "user";
	private AuthenticationManager authenticationManager;
	
	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		Optional<String> username = Optional.fromNullable(httpRequest.getHeader("X-Auth-Username"));
        Optional<String> password = Optional.fromNullable(httpRequest.getHeader("X-Auth-Password"));
        Optional<String> token = Optional.fromNullable(httpRequest.getHeader("X-Auth-Token"));
        
        String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);
     try{   
        if(checkIfSentToAuthenicateURL(resourcePath, httpRequest)){
        	log.debug("Trying to authenticate user {} by X-Auth-Username method", username);
        	authenicationUserAndPassword(username, password, httpResponse);
        }
        logger.debug("AuthenticationFilter is passing request down the filter chain");
        addSessionContextToLogging();
        chain.doFilter(request, response);
    } catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
        SecurityContextHolder.clearContext();
        logger.error("Internal authentication service exception", internalAuthenticationServiceException);
        httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    } catch (AuthenticationException authenticationException) {
        SecurityContextHolder.clearContext();
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
    } finally {
        MDC.remove(TOKEN_SESSION_KEY);
        MDC.remove(USER_SESSION_KEY);
    }
		
	}

	private void addSessionContextToLogging() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String tokenValue = "EMPTY";
        if (authentication != null && !Strings.isNullOrEmpty(authentication.getDetails().toString())) {
            MessageDigestPasswordEncoder encoder = new MessageDigestPasswordEncoder("SHA-1");
            tokenValue = encoder.encodePassword(authentication.getDetails().toString(), "not_so_random_salt");
        }
        MDC.put(TOKEN_SESSION_KEY, tokenValue);

        String userValue = "EMPTY";
        if (authentication != null && !Strings.isNullOrEmpty(authentication.getPrincipal().toString())) {
            userValue = authentication.getPrincipal().toString();
        }
        MDC.put(USER_SESSION_KEY, userValue);
    }
	private void authenicationUserAndPassword(Optional<String> username, Optional<String> password, HttpServletResponse httpResponse) throws IOException {
		logger.debug("Check authenicationUserAndPassword, username:" + username + ", password:" + password);
		Authentication resultOfAuthentication = tryToAuthenticateWithUsernameAndPassword(username, password);
		SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        log.debug("SPRAWDZAM:" + resultOfAuthentication.getDetails().toString());
        TokenResponse tokenResponse = new TokenResponse(resultOfAuthentication.getDetails().toString());
        String tokenJsonResponse = new ObjectMapper().writeValueAsString(tokenResponse);
        httpResponse.addHeader("Content-Type", "application/json");
        httpResponse.getWriter().print(tokenJsonResponse);
	}

	private Authentication tryToAuthenticateWithUsernameAndPassword(Optional<String> username, Optional<String> password) {
		logger.debug("Check tryToAuthenticateWithUsernameAndPassword, username:" + username); 
		UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(username, password);
		return tryToAuthenticate(requestAuthentication);
	}

	private Authentication tryToAuthenticate(UsernamePasswordAuthenticationToken requestAuthentication) {
		logger.debug("Check tryToAuthenticate, username:" + requestAuthentication.getName()); 
		 Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
	        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
	            throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
	        }
	        logger.debug("User successfully authenticated");
	        return responseAuthentication;
	}

	private boolean checkIfSentToAuthenicateURL(String resourcePath, HttpServletRequest httpRequest) {
		return ApiController.AUTH_URL.equalsIgnoreCase(resourcePath) && httpRequest.getMethod().equals("POST");
	}

}
