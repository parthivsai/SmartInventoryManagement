package com.mybootapp.main.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
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
import com.mybootapp.main.model.CustomerProduct;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.service.CustomerProductService;
import com.mybootapp.main.service.CustomerService;
import com.mybootapp.main.service.InwardRegisterService;
import com.mybootapp.main.service.ProductService;

@RestController
@RequestMapping("/customer-product")
public class CustomerProductController {

	// If we don't use Autowired we will get null pointer exception. 
	// Spring automatically autowires for us.
	@Autowired
	private CustomerService customerService; 
	
	@Autowired
	private ProductService productService; 
	
	@Autowired
	private CustomerProductService customerProductService;
	
	@Autowired
	private InwardRegisterService inwardRegisterService;

	/* 
	 PATH: /customer-product/purchase/{customerId}/{productId}
	 Method: POST
	 RequestBody: CustomerProduct customerProduct
	 response: customerProduct 
	 PathVariable: customerId, productId
	 */
	@PostMapping("/purchase/{customerId}/{productId}")
	public ResponseEntity<?> purchaseApi(@RequestBody CustomerProduct customerProduct,
							@PathVariable("customerId") int customerId, 
							@PathVariable("productId") int productId) {

		/* Validate Ids and fetch respective objects  */
		Customer customer  = customerService.getById(customerId);
		if(customer == null )
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid customer ID given");

		Product product = productService.getById(productId);
		if(product == null )
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid product ID given");

		/* Attach customer and product to customerProduct*/
		customerProduct.setCustomer(customer);
		customerProduct.setProduct(product);
		customerProduct.setDateOfPurchase(LocalDate.now());

		/* Check if that product is available in proper quantity in InwardRegister*/
		boolean status = inwardRegisterService.checkQuantity(productId,customerProduct.getQuantityPurchased());
		if(status == false)
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
					.body("Product out of stock");

		/* Save customerProduct object in DB */
		customerProduct = customerProductService.insert(customerProduct);
		return ResponseEntity.status(HttpStatus.OK).body(customerProduct);
	}
	
	/* 
	 PATH: /customer-product/all
	 Method: GET
	 RequestBody: None
	 response: List<CustomerProduct> 
	 PathVariable: None
	 */
	@GetMapping("/all")
	public List<CustomerProduct> getCustomerProducts() {
		/* Fetch all the records from CustomerProduct and return them as a list */
		List<CustomerProduct> customerProduct = customerProductService.getAllCustomerProducts();
		return customerProduct;
	}
	
	/* 
	 PATH: /customer-product/one/{id}
	 Method: GET
	 RequestBody: None
	 response:CustomerProduct 
	 PathVariable: id
	 */
	@GetMapping("/one/{id}")
	public ResponseEntity<?> getOneManager(@PathVariable("id") int id) {
		
		/* Check if the given Id is valid and return the customerProduct */
		CustomerProduct customerProduct = customerProductService.getById(id);
		if(customerProduct ==  null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid customerProductID!!");
		}
		return ResponseEntity.status(HttpStatus.OK).body(customerProduct);
	}
	
	/* 
	 PATH: /customer-product/update/{id}/{customerId}/{productId}
	 Method: PUT
	 RequestBody: CustomerProduct newCustomerProduct
	 response: newCustomerProduct 
	 PathVariable: id, customerId, productId
	 */
	@PutMapping("/update/{id}/{customerId}/{productId}")
	public ResponseEntity<?> updateCustomerProduct(@PathVariable("id") int id,
			@PathVariable("customerId") int customerId,
			@PathVariable("productId") int productId,
			@RequestBody CustomerProduct newCustomerProduct){
		
		/* check if all the given Id's are valid and store them in respective objects */
		CustomerProduct oldCustomerProduct = customerProductService.getById(id);
		if(oldCustomerProduct == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid CustomerProductID given!!");
		}
		Customer customer  = customerService.getById(customerId);
		if(customer == null ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid customer ID given");
		}
		Product product = productService.getById(productId);
		if(product == null ) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product ID given");
		}
		
		/* Attach customer and product to newCustomerProduct*/
		newCustomerProduct.setId(oldCustomerProduct.getId());
		newCustomerProduct.setProduct(product);
		newCustomerProduct.setCustomer(customer);
		newCustomerProduct.setDateOfPurchase(LocalDate.now());
		
		/* Check if that product is available in proper quantity in InwardRegister before allowing to update */
		boolean status = inwardRegisterService.checkQuantity(productId,newCustomerProduct.getQuantityPurchased());
		if(status == false)
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Product out of stock");
		
		/* save in the database */
		newCustomerProduct = customerProductService.insert(newCustomerProduct);
		return ResponseEntity.status(HttpStatus.OK).body(newCustomerProduct);
	}
	
	/* 
	 PATH: /customer-product/delete/{id}
	 Method: DELETE
	 RequestBody: None
	 response: String 
	 PathVariable: id
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteManager(@PathVariable("id") int id){
		
		/* check if the given Id is valid */
		CustomerProduct customerProduct = customerProductService.getById(id);
		if(customerProduct == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid CustomerProductID given!!");
		}
		
		/* delete the record from the database */
		customerProductService.deleteManager(customerProduct);
		return ResponseEntity.status(HttpStatus.OK).body("Successfully Deleted!");
	}
	
}