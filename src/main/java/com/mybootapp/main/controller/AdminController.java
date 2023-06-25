package com.mybootapp.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mybootapp.main.model.User;
import com.mybootapp.main.service.MyUserService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	// If we don't use Autowired we will get null pointer exception. 
	// SpringBoot automatically autowires it for us.
	@Autowired
	private PasswordEncoder encoder; 

	@Autowired
	private MyUserService userService;

	/* 
	 PATH: /admin/add
	 Method: POST
	 RequestBody: User user
	 response: User 
	 PathVariable: none
	 */
	@PostMapping("/add")
	public User add(@RequestBody User user) {
		/*encode the password, set the role, save it in DB */
		user.setPassword(encoder.encode(user.getPassword()));
		user.setRole("ADMIN");
		return userService.insert(user);
	}
	
	/* 
	 PATH: /admin/delete/{userId}
	 Method: DELETE
	 RequestBody: none
	 response: String 
	 PathVariable: ID
	 */
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable("userId") int userId){
		
		/* check if the given userId is valid */
		User user = userService.getById(userId);
		if(user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid userId!!");
		}
		
		/* delete record from the database */
		userService.deleteUser(user);
		return ResponseEntity.status(HttpStatus.OK).body("Deleted Successfully!");
	}
}
