package com.mybootapp.main.controller;

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

import com.mybootapp.main.model.Customer;
import com.mybootapp.main.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	// If we don't use Autowired we will get null pointer exception. 
	// SpringBoot automatically autowires it for us.
	@Autowired
	private CustomerService customerService;

	/* 
	 PATH: /customer/add
	 Method: POST
	 RequestBody: Customer customer
	 response: customer 
	 PathVariable: none
	 */
	@PostMapping("/add")
	public Customer postCustomer(@RequestBody Customer customer) {
		/* Save the customer into database */
		return customerService.insert(customer);
	}

	/* 
	 PATH: /customer/all
	 Method: GET
	 RequestBody: None
	 response: List<Customer> 
	 PathVariable: None
	 */
	@GetMapping("/all")
	public List<Customer> getAllCustomers() {
		/* get all the customers from the database and return them in a list */
		return customerService.getCustomers();
	}
	
	/* 
	 PATH: /customer/one/{id}
	 Method: GET
	 RequestBody: None
	 response: customer 
	 PathVariable: id
	 */
	@GetMapping("/one/{id}")
	public ResponseEntity<?> getoneCustomer(@PathVariable("id") int id) {
		
		/* Validate the given Customer Id and return the customer */
		Customer customer = customerService.getById(id);
		if(customer == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID given");
		}
		return ResponseEntity.status(HttpStatus.OK).body(customer);
	}
	
	/* 
	 PATH: /customer/update/{customerId}
	 Method: PUT
	 RequestBody: Customer newCustomer
	 response: newCustomer 
	 PathVariable: customerId
	 */
	@PutMapping("/update/{customerId}")
	public ResponseEntity<?> updateCustomer(@PathVariable("customerId") int customerId, @RequestBody Customer newCustomer){
		
		/* Validate the given customerId */
		Customer oldCustomer = customerService.getById(customerId);
		if(oldCustomer == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Customer Id given!");
		}
		
		/* set the Id of oldCustomer to newCustomer */
		newCustomer.setId(oldCustomer.getId());
		
		/* Save newCustomer in the database */
		customerService.insert(newCustomer);
		
		return ResponseEntity.status(HttpStatus.OK).body(newCustomer);
	}
	
	/* 
	 PATH: /customer/delete/{customerID}
	 Method: DELETE
	 RequestBody: None
	 response: String 
	 PathVariable: customerID
	 */
	@DeleteMapping("/delete/{customerID}")
	public ResponseEntity<?> deleteCustomer(@PathVariable("customerID") int customerID){
		
		/* Validate the given customerId */
		Customer customer = customerService.getById(customerID);
		if(customer == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Customer Id given!");
		}
		
		/* Delete record from the database */
		customerService.deleteCustomer(customer);
		
		return ResponseEntity.status(HttpStatus.OK).body("Customer deleted Successfully!");
	}
	
}
