package pl.peetross.rest.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//Simple Basic Authorization is sending in header: Autorization: Basic #encoded Base64 user+pass
		//if session policy is always either by similar is returning jsesionId=sessionId and another authentication is not need
		//httpBasic() needed
		http
			.csrf().disable()
			.authorizeRequests()
				.antMatchers("/hello").permitAll()
				.antMatchers("/greeting").hasRole("USER")
				//.anyRequest().authenticated()
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());
			//.and()
			//.anonymous().disable();
		http
			.addFilterBefore(new AuthenticationFilter(authenticationManager()), BasicAuthenticationFilter.class);
		
		
	
		
	}	
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.authenticationProvider(userRoleUsernamePasswordAuthenticationProvider());
	            
	}
	@Bean
	public AuthenticationEntryPoint unauthorizedEntryPoint() {
		return new AuthenticationEntryPoint() {
			
			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
			
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				
			}
		};
		//return (request, response, authException).response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
	
	@Bean
	public AuthenticationProvider userRoleUsernamePasswordAuthenticationProvider() {
		return new UserRoleUsernamePasswordAuthenticationProvider();
	}



}
