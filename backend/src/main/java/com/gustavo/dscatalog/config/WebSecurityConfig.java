package com.gustavo.dscatalog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder); // with this configuration, Spring
																						// Security, during the
																						// authentication process, will
																						// know how to find the user by
																						// email (by userDetailsService
																						// object that we have passed in
																						// the parameter) and how to
																						// analyze the encrypt of
																						// password
		super.configure(auth);
	}// we will need to configure the algorithm that we are using to encrypt the
		// password

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/actuator/**");
	}

	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception { // with bean annotation, we can turn this method available in our system as a configuration.
		return super.authenticationManager();
	}
	
	
}