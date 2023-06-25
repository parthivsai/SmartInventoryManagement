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

import com.mybootapp.main.exception.ResourceNotFound;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.service.ProductService;

@RestController
@RequestMapping("/product")
public class ProductController {

	// If we don't use Autowired we will get null pointer exception. 
	// SpringBoot automatically autowires it for us.
	@Autowired
	private ProductService productService; // Injecting Service in Controller : DI(Dependency Injection)
	
	/* 
	 PATH: /product/add
	 Method: POST
	 RequestBody: Product product
	 response: product 
	 PathVariable: none
	 */
	@PostMapping("/add")
	public ResponseEntity<?> postProduct(@RequestBody Product product) {
		/* Save the product into database */
		product = productService.insert(product);
		return  ResponseEntity.status(HttpStatus.OK).body(product);
	}
	
	/* 
	 PATH: /product/all
	 Method: GET
	 RequestBody: None
	 response: List<Product> 
	 PathVariable: None
	 */
	@GetMapping("/all")
	public List<Product> getAllProducts() {
		/* Get all products and return the list */
		List<Product> list =  productService.getAllProducts();
		return list; 
	}
	
	/* 
	 PATH: /product/one/{id}
	 Method: GET
	 RequestBody: None
	 response: Product 
	 PathVariable: ID
	 */
	@GetMapping("/one/{id}")
	public ResponseEntity<?> getProduct(@PathVariable("id") int id) {
		/* Validate the given product Id and return the product if Id is valid */
		Product product  = productService.getproduct(id);
		if(product == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID given");
		}
		return ResponseEntity.status(HttpStatus.OK).body(product); 
	}
	
	//Not a professional way
	@GetMapping("/one/alternate/{id}")
	public Object getProductAlternate(@PathVariable("id") int id) {
		try {
			Product product  = productService.getproductAlternate(id);
			return product; 
		} catch (ResourceNotFound e) {
			 return e.getMessage();
		}
	}
	
	/* 
	 PATH: /product/update/{id}
	 Method: PUT
	 RequestBody: Product newProduct
	 response: Product 
	 PathVariable: ID
	 */
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable("id") int id, @RequestBody Product newProduct) {
		/* Validation for request body of newProduct */
		if(newProduct.getTitle() == null || !newProduct.getTitle().trim().matches("[a-zA-Z0-9- *]+"))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Title has to have valid format [a-zA-Z0-9- ]");
		
		if(newProduct.getDescription() == null || newProduct.getDescription().equals(""))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Description cannot be nullor blank");
		
		if(newProduct.getPrice() == 0)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Price must have a value other than 0");
		
		/* Validate the given product id */
		Product oldProduct  = productService.getproduct(id);
		if(oldProduct == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		/* There are two ways to do it
		 * 1. Transfer new values to old (that has existing id)
		 * 2. Transfer id from old to new (preferred way).  
		 */
		newProduct.setId(oldProduct.getId());
		
		/* save the newProduct into database */
	    newProduct = productService.insert(newProduct);
	    return ResponseEntity.status(HttpStatus.OK).body(newProduct);
	}
	
	
	/* 
	 PATH: /product/delete/{id}
	 Method: DELETE
	 RequestBody: None
	 response: String 
	 PathVariable: id
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable("id") int id) {
		/* Validate product id */
		Product product  = productService.getproduct(id);
		if(product == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ID given");
		}
		
		/* Delete the product from database */
		productService.deleteProduct(product);
		
		return ResponseEntity.status(HttpStatus.OK).body("Product deleted..");
	}
}