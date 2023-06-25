package com.mybootapp.main.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mybootapp.main.model.User;
import com.mybootapp.main.service.MyUserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	// If we don't use Autowired we will get null pointer exception. 
	// SpringBoot automatically autowires it for us.
	@Autowired
	private MyUserService userService;
	
	/* 
	 PATH: /user/login
	 Method: GET
	 RequestBody: none
	 response: UserDetails 
	 PathVariable: none
	 */
	@GetMapping("/login") // By the time we reach here spring has already validated userName and password
	public UserDetails login(Principal principal) {
		// need username from spring so that we can go to database and fetch the role.
		String username = principal.getName();
		
		UserDetails user = userService.loadUserByUsername(username);
		
		return user;
		
	}
	
}
