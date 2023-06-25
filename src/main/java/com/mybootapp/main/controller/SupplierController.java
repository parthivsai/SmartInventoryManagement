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
import com.mybootapp.main.model.Supplier;
import com.mybootapp.main.service.SupplierService;

@RestController
@RequestMapping("/supplier")
public class SupplierController {
	
	// If we don't use Autowired we will get null pointer exception. 
	// SpringBoot automatically autowires it for us.
	@Autowired
	private SupplierService supplierService;
	
	/* 
	 PATH: /supplier/add
	 Method: POST
	 RequestBody: Supplier supplier
	 response: supplier 
	 PathVariable: none
	 */
	@PostMapping("/add")
	public void insertSupplier(@RequestBody Supplier supplier) {
		/* save the supplier in the database */
		supplierService.insert(supplier);
	}
	
	/* 
	 PATH: /supplier/all
	 Method: GET
	 RequestBody: none
	 response: List<Supplier> 
	 PathVariable: none
	 */
	@GetMapping("/all")
	public List<Supplier> getAllSuppliers() {
		/* fetch all suppliers and return them in a list */
		return supplierService.getSuppliers();
	}
	
	/* 
	 PATH: /supplier/one/{supplierId}
	 Method: GET
	 RequestBody: none
	 response: supplier
	 PathVariable: supplierId
	 */
	@GetMapping("/one/{id}")
	public ResponseEntity<?> getoneSupplier(@PathVariable("supplierId") int supplierId) {
		/* validate the Id and fetch the record */
		Supplier supplier = supplierService.getById(supplierId);
		if(supplier == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID given");
		}
		return ResponseEntity.status(HttpStatus.OK).body(supplier);
	}
	
	/* 
	 PATH: /supplier/update/{supplierId}
	 Method: PUT
	 RequestBody: Supplier newSupplier
	 response: newSupplier 
	 PathVariable: supplierId
	 */
	@PutMapping("/update/{supplierId}")
	public ResponseEntity<?> updateCustomer(@PathVariable("supplierId") int supplierId, @RequestBody Supplier newSupplier){
		/* validate the supplier id */
		Supplier oldSupplier = supplierService.getById(supplierId);
		if(oldSupplier == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Supplier Id given!");
		}
		
		/* transfer the oldSupplier Id to newSupplier and save it in the database */
		newSupplier.setId(oldSupplier.getId());
		supplierService.insert(newSupplier);
		
		return ResponseEntity.status(HttpStatus.OK).body(newSupplier);
	}
	
	/* 
	 PATH: /supplier/delete/{supplierId}
	 Method: DELETE
	 RequestBody: none
	 response: String 
	 PathVariable: supplierId
	 */
	@DeleteMapping("/delete/{supplierId}")
	public ResponseEntity<?> deleteCustomer(@PathVariable("supplierId") int supplierId){
		
		/* validate the supplier id and fetch the object */
		Supplier supplier = supplierService.getById(supplierId);
		if(supplier == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Supplier Id given!");
		}
		
		/* delete the record from the database */
		supplierService.deleteSupplier(supplier);
		return ResponseEntity.status(HttpStatus.OK).body("Supplier deleted Successfully!");
	}
}
