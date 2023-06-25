package com.mybootapp.main.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mybootapp.main.model.Godown;
import com.mybootapp.main.model.Manager;
import com.mybootapp.main.service.GodownService;
import com.mybootapp.main.service.ManagerService;

@RestController
@RequestMapping("/godown")
public class GodownController {
	
	// If we don't use Autowired we will get null pointer exception. 
	// SpringBoot automatically autowires it for us.
	@Autowired
	private GodownService godownService;
	
	@Autowired
	private ManagerService managerService; 
	
	/* 
	 PATH: /godown/add/{managerId}
	 Method: POST
	 RequestBody: Godown godown
	 response: godown 
	 PathVariable: managerId
	 */
	@PostMapping("/add/{managerID}")
	public ResponseEntity<?> insertGodown(@PathVariable("managerID") int managerID, 
			@RequestBody Godown godown) {
		//Step 0: validation, if needed for Request body, is done is ProductController in PUT api. 
		
		/* Validate and fetch Manager from managerId */
		Manager manager = managerService.getById(managerID);
		if(manager == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Manager ID"); 
		
		/* Attach manager to godown object */
			godown.setManager(manager);
			
		/* Save godown object */
		godown = godownService.insert(godown);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(godown);
	}
	
	/* 
	 PATH: /godown/all
	 Method: GET
	 RequestBody: None
	 response: List<Godown> 
	 PathVariable: None
	 */
	@GetMapping("/all")
	public List<Godown> getGodowns() {
		/* get all godowns and return them as a list */
		List<Godown> godowns = godownService.getAllGodowns();
		return godowns;
	}
	
	/* 
	 PATH: /godown/one/{godownId}
	 Method: GET
	 RequestBody: None
	 response: godown 
	 PathVariable: godownId
	 */
	@GetMapping("/one/{godownId}")
	public ResponseEntity<?> getOneManager(@PathVariable("godownId") int godownId) {
		
		/* validate the godownId and fetch the godown */
		Godown godown = godownService.getById(godownId);
		if(godown ==  null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid godownID!!");
		}
		return ResponseEntity.status(HttpStatus.OK).body(godown);
	}
	
	/* 
	 PATH: /godown/update/{godownId}/{managerId}
	 Method: PUT
	 RequestBody: Godown newGodown
	 response: newGodown 
	 PathVariable: godownId
	 */
	@PutMapping("/update/{godownId}/{managerId}")
	public ResponseEntity<?> updateGodown(@PathVariable("godownId") int godownId,
			@PathVariable("managerId") int managerId,
			@RequestBody Godown newGodown){
		
		/* validate and fetch objects based on given id's */
		Godown oldGodown = godownService.getById(godownId);
		if(oldGodown == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid GodownID given!!");
		}
		
		/* Validate and fetch Manager from managerId */
		Manager manager = managerService.getById(managerId);
		if(manager == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Manager ID");
		}
		
		/* set the Id of oldGodown to newGodown and assign the manager */
		newGodown.setId(oldGodown.getId());
		newGodown.setManager(manager);
		
		newGodown = godownService.insert(newGodown);

		return ResponseEntity.status(HttpStatus.OK).body(newGodown);
	}
	
	/* 
	 PATH: /godown/delete/{godownId}
	 Method: DELETE
	 RequestBody: None
	 response: String 
	 PathVariable: godownId
	 */
	@DeleteMapping("/delete/{godownId}")
	public ResponseEntity<?> deleteGodown(@PathVariable("godownId") int godownId){
		
		/* check if the given Id is valid */
		Godown godown = godownService.getById(godownId);
		if(godown == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid GodownID given!!");
		}
		
		/* delete the record from the database*/
		godownService.deleteGodown(godown);
		return ResponseEntity.status(HttpStatus.OK).body("Successfully Deleted!");
	}
	
	/* 
	 PATH: /godown/report
	 Method: GET
	 RequestBody: None
	 response: List<Godown> 
	 PathVariable: None
	 */
	@GetMapping("/report")
	public List<Godown> godownReport(){
		/* fetch all the records of Godown and return them in a list */
		List<Godown> list = godownService.getAllGodowns();
		return list;
	}
}
