package com.mybootapp.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mybootapp.main.service.MyUserService;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MyUserService myUserService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Building my own AuthManager that will read user details from the DB
		/*
		 * auth.inMemoryAuthentication()
		 * .withUser("ronaldo@incedo.com").password(getEncoder().encode("portugal")).
		 * authorities("ADMIN") .and()
		 * .withUser("messi@incedo.com").password(getEncoder().encode("argentina")).
		 * authorities("USER");
		 */
		// point spring auth to DB
		auth.authenticationProvider(getProvider());
	}

	private AuthenticationProvider getProvider() {
		DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
		dao.setPasswordEncoder(getEncoder());
		// to point to DB, go to service first.
		dao.setUserDetailsService(myUserService);
		return dao;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// List of my api's along with User permissions
		// Assigning permissions to my api's according to the requirements
		http.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/user/login").authenticated()
				.antMatchers(HttpMethod.POST, "/inwardregister/add/{productId}/{godownId}/{supplierId}").hasAuthority("MANAGER")
				.antMatchers(HttpMethod.PUT, "/inwardregister/update/{id}/{productId}/{godownId}/{supplierId}").hasAuthority("MANAGER")
				.antMatchers(HttpMethod.DELETE, "/inwardregister/delete/{id}").hasAuthority("MANAGER")
				.antMatchers(HttpMethod.POST, "/customer/add").permitAll()
				.antMatchers(HttpMethod.POST, "/product/add").hasAnyAuthority("MANAGER", "USER")
				.antMatchers(HttpMethod.PUT, "/product/update/{id}").hasAnyAuthority("MANAGER", "USER")
				.antMatchers(HttpMethod.DELETE, "/product/delete/{id}").hasAnyAuthority("MANAGER", "USER")
				.antMatchers(HttpMethod.POST, "/supplier/add").authenticated()
				.antMatchers(HttpMethod.PUT, "/supplier/update/{supplierId}").authenticated()
				.antMatchers(HttpMethod.DELETE, "/supplier/delete/{supplierId}").authenticated()
				.antMatchers(HttpMethod.POST, "/admin/add").permitAll()
				.antMatchers(HttpMethod.POST, "/manager/add").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.PUT, "/manager/update/{managerId}").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/manager/delete/{managerId}").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.POST, "/employee/add/{managerId}").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.PUT, "/employee/update/{employeeId}/{managerId}").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.DELETE, "/employee/delete/{id}").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.GET, "/inwardregister/report").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.GET, "/outwardregister/report").hasAuthority("ADMIN")
				.antMatchers(HttpMethod.GET, "/godown/report").hasAuthority("ADMIN")
				.anyRequest().permitAll()
				.and()
				.httpBasic()
				.and()
				.csrf().disable();
	}

	@Bean
	public PasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}
}