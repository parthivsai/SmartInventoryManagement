package com.mybootapp.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mybootapp.main.model.Manager;
import com.mybootapp.main.model.User;
import com.mybootapp.main.service.ManagerService;
import com.mybootapp.main.service.MyUserService;

@RestController
@RequestMapping("/manager")
public class ManagerController {

	// If we don't use Autowired we will get null pointer exception. 
	// SpringBoot automatically autowires it for us.
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private MyUserService userService;

	/* 
	 PATH: /manager/add
	 Method: POST
	 RequestBody: Manager manager
	 response: manager 
	 PathVariable: none
	 */
	@PostMapping("/add")
	public Manager postManager(@RequestBody Manager manager) {
		/*Read user info given as input and attach it to user object.  */
		User user = manager.getUser();
		user.setRole("MANAGER");
		
		/* Encode the password before saving in DB */
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		/* Save user in DB and fetch saved object */
		user = userService.insert(user);
		
		/* attach user to manager */
		manager.setUser(user);
		
		/* Save manager in DB */
		return managerService.insert(manager);
	}

	/* 
	 PATH: /manager/all
	 Method: GET
	 RequestBody: none
	 response: List<Manager> 
	 PathVariable: none
	 */
	@GetMapping("/all")
	public List<Manager> getManagers() {
		/* fetch all the managers and return them as a list */
		List<Manager> managers = managerService.getAllManagers();
		return managers;
	}
	
	/* 
	 PATH: /manager/one/{managerId}
	 Method: GET
	 RequestBody: none
	 response: manager 
	 PathVariable: managerId
	 */
	@GetMapping("/one/{managerId}")
	public ResponseEntity<?> getOneManager(@PathVariable("managerId") int managerId) {
		/* validate the Id and fetch the manager */
		Manager manager = managerService.getById(managerId);
		if(manager ==  null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid managerID!!");
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(manager);
	}
	
	/* 
	 PATH: /manager/update/{managerId}
	 Method: PUT
	 RequestBody: Manager newManager
	 response: newManager 
	 PathVariable: managerId
	 */
	@PutMapping("/update/{managerId}")
	public ResponseEntity<?> updateManager(@PathVariable("managerId") int managerId, @RequestBody Manager newManager){
		
		/* validate the given managerId and fetch the oldManager record */
		Manager oldManager = managerService.getById(managerId);
		if(oldManager == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid managerID given!!");
		}
		
		/* transfer the oldManager Id to newManager and save the record */
		newManager.setId(oldManager.getId());
		newManager = managerService.insert(newManager);

		return ResponseEntity.status(HttpStatus.OK).body(newManager);
	}
	
	/* 
	 PATH: /manager/delete/{managerId}
	 Method: DELETE
	 RequestBody: none
	 response: String
	 PathVariable: managerId
	 */
	@DeleteMapping("/delete/{managerId}")
	public ResponseEntity<?> deleteManager(@PathVariable("managerId") int managerId){
		
		/* validate the Id and fetch the record */
		Manager manager = managerService.getById(managerId);
		if(manager == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid managerID given!!");
		}
		/* delete the record from the database */
		managerService.deleteManager(manager);
		return ResponseEntity.status(HttpStatus.OK).body("Successfully Deleted!");
	}

}
