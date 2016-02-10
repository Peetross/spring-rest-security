package pl.peetross.rest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.peetross.rest.api.ApiController;

/**
 *	@author <a href="mailto:peetross@gmail.com">Piotr Mszyca</a>
 */
@RestController
public class AuthenticationController extends ApiController{

	@RequestMapping(value = AUTH_URL, method = RequestMethod.POST)
	public String activate(){
		return "activated";
	}
	
}
