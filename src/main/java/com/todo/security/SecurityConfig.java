package com.todo.security;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.inMemoryAuthentication().withUser("Ahmad").password("{noop}123")
				.roles("ADMIN").and().withUser("Belal").password("{noop}123")
				.roles("USER");
	}

	
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().and().authorizeRequests()
				.antMatchers("/todo/items/").access("hasRole('USER')")
				.antMatchers("/todo/items").access("!hasRole('ADMIN')")
				.antMatchers("/todo/items/all").access("hasRole('ADMIN')")
				.antMatchers("/todo/items/all/**").access("!hasRole('USER')")
				.and()
				.csrf().disable().headers().frameOptions().disable();
	}

}