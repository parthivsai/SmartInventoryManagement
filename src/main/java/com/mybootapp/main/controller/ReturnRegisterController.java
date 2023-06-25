package com.mybootapp.main.controller;

import java.time.LocalDate;
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
import com.mybootapp.main.model.OutwardRegister;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.model.ReturnRegister;
import com.mybootapp.main.service.GodownService;
import com.mybootapp.main.service.ReturnRegisterService;

@RestController
@RequestMapping("/returnregister")
public class ReturnRegisterController {
	
	// If we don't use Autowired we will get null pointer exception. 
	// SpringBoot automatically autowires it for us.
	@Autowired
	private ReturnRegisterService returnRegisterService;
	
	@Autowired
	private GodownService godownService;
	
	/* 
	 PATH: /returnregister/add/{godownId}
	 Method: POST
	 RequestBody: ReturnRegister returnRegister
	 response: returnRegister 
	 PathVariable: godownId
	 */
	@PostMapping("/add/{godownId}")
	public ResponseEntity<?> addReturn(@PathVariable("godownId") int godownId, @RequestBody ReturnRegister returnRegister){
		
		/* Check if the given godownId is valid and fetch the godown if valid */
		Godown godown = godownService.getById(godownId);
		if(godown == null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Invalid godownId given. Please try again with correct godownId.");   
		}
	
		/* Attach all objects to returnRegister */
		returnRegister.setGodown(godown);
		returnRegister.setDateOfReturn(LocalDate.now());
	
		/* Save returnRegister */
		returnRegister = returnRegisterService.insert(returnRegister);
		return ResponseEntity.status(HttpStatus.OK).body(returnRegister);
	}
	
	/* 
	 PATH: /returnregister/all
	 Method: GET
	 RequestBody: none
	 response: List<ReturnRegister>
	 PathVariable: none
	 */
	@GetMapping("/all")
	public List<ReturnRegister> getAllReturns(){
		/* fetch all Return records from the db and return it */
		List<ReturnRegister> returns = returnRegisterService.getAllReturns();
		return returns;
	}
	
	/* 
	 PATH: /returnregister/one/{returnId}
	 Method: GET
	 RequestBody: ReturnRegister returnRecord
	 response: returnRecord 
	 PathVariable: returnId
	 */
	@GetMapping("/one/{returnId}")
	public ResponseEntity<?> getReturn(@PathVariable("returnId") int returnId) {
		/* Check if the given id is valid and fetch the record from db and return it */
		ReturnRegister returnRecord = returnRegisterService.getById(returnId);
		if(returnRecord == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid returnId given. Try again with correct Id");
		}
		return ResponseEntity.status(HttpStatus.OK).body(returnRecord);
	}
	
	/* 
	 PATH: /returnregister/update/{returnId}/{godownId}
	 Method: PUT
	 RequestBody: ReturnRegister newReturn
	 response: newReturn 
	 PathVariable: returnId, godownId
	 */
	@PutMapping("/update/{returnId}/{godownId}")
	public ResponseEntity<?> updateReturn(@PathVariable("returnId") int returnId,
			@PathVariable("godownId") int godownId,
			@RequestBody ReturnRegister newReturn){
		/* Check if all the id's are valid and fetch the objects */
		ReturnRegister oldReturn = returnRegisterService.getById(returnId);
		if(oldReturn == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid returnId given. Try again with correct Id");
		}
		Godown godown = godownService.getById(godownId);
		if(godown == null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Invalid godownId given. Please try again with correct godownId.");   
		}
		
		/* update the fields and transfer Id of oldObject to newObject */
		newReturn.setDateOfReturn(LocalDate.now());
		newReturn.setGodown(godown);
		newReturn.setId(oldReturn.getId());
			
		/* Save the newReturn in the database*/
		newReturn = returnRegisterService.insert(newReturn);
			
		return ResponseEntity.status(HttpStatus.OK).body(newReturn);
	}
		
	/* 
	 PATH: /returnregister/delete/{returnId}
	 Method: DELETE
	 RequestBody: none
	 response: String 
	 PathVariable: returnId
	 */
	@DeleteMapping("/delete/{returnId}")
	public ResponseEntity<?> deleteReturn(@PathVariable("returnId") int returnId){
			
		/* validate the Id and fetch the object to be deleted */
		ReturnRegister returnRecord = returnRegisterService.getById(returnId);
		if(returnRecord == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid returnId given. Try again with correct Id");
		}
			
		/* delete the returnRecord from the database */
		returnRegisterService.deleteReturn(returnRecord);
		return ResponseEntity.status(HttpStatus.OK).body("Return deleted Successfully!!");
	}
	
}
