package pl.peetross.rest.controllers;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pl.peetross.rest.api.ApiController;

/**
 *	@author <a href="mailto:peetross@gmail.com">Piotr Mszyca</a>
 */

@RestController
public class RootController extends ApiController{
	
	private Logger log = Logger.getLogger(RootController.class);
	
	
	
	@RequestMapping(GREETING_URL)
	public String greeting(@RequestParam(value="name") String name){
		log.info("TEST info greeting:" + name);
		return name + " greeting";
	}
	
	@RequestMapping(HELLO_URL)
	public String hello(){
		log.info("TEST info hello:");
		return "helka";
	}

}
