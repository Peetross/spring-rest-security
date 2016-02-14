package pl.peetross.rest.security;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *	@author <a href="mailto:peetross@gmail.com">Piotr Mszyca</a>
 */
public class TokenResponse {
	
	@JsonProperty
	public String token;

	public TokenResponse(){}
	
	public TokenResponse(String token) {
		super();
		this.token = token;
	}
	

}
