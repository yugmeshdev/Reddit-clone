package com.example.springredditclone.config;
//complete security for be

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
	//csrf attacks mainly occur when there are sessions and cookies for authentication we are using json web token so we are disabling it here
	//any request which does not follow api pattern should be authenticated
	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable()
		.authorizeRequests()
		.antMatchers("/api/auth/**")
		.permitAll()
		.anyRequest()
		.authenticated();
	}
	
}
