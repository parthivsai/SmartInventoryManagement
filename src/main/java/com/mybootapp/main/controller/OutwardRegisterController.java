package com.mybootapp.main.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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

import com.mybootapp.main.Dto.OutwardRegisterDto;
import com.mybootapp.main.model.Customer;
import com.mybootapp.main.model.CustomerProduct;
import com.mybootapp.main.model.Godown;
import com.mybootapp.main.model.OutwardRegister;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.service.CustomerProductService;
import com.mybootapp.main.service.CustomerService;
import com.mybootapp.main.service.GodownService;
import com.mybootapp.main.service.OutwardRegisterService;
import com.mybootapp.main.service.ProductService;

@RestController
@RequestMapping("/outwardregister")
public class OutwardRegisterController {

	// If we don't use Autowired we will get null pointer exception.
	// Spring automatically autowires for us.
	@Autowired
	private OutwardRegisterService outwardRegisterService;

	@Autowired
	private ProductService productService;

	@Autowired
	private GodownService godownService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerProductService customerProductService;

	/*
	 * PATH: /outwardregister/add/{productId}/{godownId}/{customerId} Method: POST
	 * RequestBody: OutwardRegister outwardRegister response: outwardRegister
	 * PathVariable: productId, godownId, customerId
	 */
	@PostMapping("/add/{productId}/{godownId}/{customerId}")
	public ResponseEntity<?> addOutwardRegister(@RequestBody OutwardRegister outwardRegister,
			@PathVariable("productId") int productId, @PathVariable("godownId") int godownId,
			@PathVariable("customerId") int customerId) {

		/* Check if all the given id's are valid and fetch the objects if valid */
		Product product = productService.getById(productId);
		if (product == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ProductId given. Please try again with correct productId.");
		}
		Godown godown = godownService.getById(godownId);
		if (godown == null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
					.body("Invalid godownId given. Please try again with correct godownId.");
		}
		Customer customer = customerService.getById(customerId);
		if (customer == null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
					.body("Invalid CustomerId given. Please try again with correct godownId.");
		}

		/* Attach all objects to outwardsRegister */
		outwardRegister.setProduct(product);
		outwardRegister.setGodown(godown);
		outwardRegister.setCustomer(customer);
		outwardRegister.setDateOfDelivery(LocalDate.now());

		/* Save outwardsRegister */
		outwardRegister = outwardRegisterService.insert(outwardRegister);
		return ResponseEntity.status(HttpStatus.OK).body(outwardRegister);

	}

	/*
	 * PATH: /outwardregister/all Method: GET RequestBody: None response:
	 * List<OutwardRegister> PathVariable: None
	 */
	@GetMapping("/all")
	public List<OutwardRegister> getAllOutwards() {
		/* fetch all Outward records from the db and return it */
		List<OutwardRegister> outwards = outwardRegisterService.getAllOutwards();
		return outwards;
	}

	/*
	 * PATH: /outwardregister/one/{outwardId} Method: GET RequestBody: None
	 * response: outwardRegister PathVariable: outwardId
	 */
	@GetMapping("/one/{outwardId}")
	public ResponseEntity<?> getOutward(@PathVariable("outwardId") int outwardId) {
		/* Check if the given id is valid and fetch the record from db and return it */
		OutwardRegister outward = outwardRegisterService.getOutward(outwardId);
		if (outward == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid outwardId given. Try again with correct Id");
		}
		return ResponseEntity.status(HttpStatus.OK).body(outward);
	}

	/*
	 * PATH: /outwardregister/update/{productId}/{godownId}/{customerId} Method: PUT
	 * RequestBody: OutwardRegister newOutward response: newOutward PathVariable:
	 * productId, godownId, customerId
	 */
	@PutMapping("/update/{outwardId}/{productId}/{godownId}/{customerId}")
	public ResponseEntity<?> updateOutward(@PathVariable("outwardId") int outwardId,
			@PathVariable("productId") int productId, @PathVariable("godownId") int godownId,
			@PathVariable("customerId") int customerId, @RequestBody OutwardRegister newOutward) {

		/* Check if all the given id's are valid and fetch the objects */
		OutwardRegister oldOutward = outwardRegisterService.getOutward(outwardId);
		if (oldOutward == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid outwardId given. Try again with correct Id");
		}
		Product product = productService.getById(productId);
		if (product == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid ProductId given. Please try again with correct productId.");
		}
		Godown godown = godownService.getById(godownId);
		if (godown == null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
					.body("Invalid godownId given. Please try again with correct godownId.");
		}
		Customer customer = customerService.getById(customerId);
		if (customer == null) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
					.body("Invalid customerId given. Please try again with correct godownId.");
		}

		/* update the fields and transfer Id of oldObject to newObject */
		newOutward.setDateOfDelivery(oldOutward.getDateOfDelivery());
		newOutward.setProduct(product);
		newOutward.setGodown(godown);
		newOutward.setCustomer(customer);
		newOutward.setId(oldOutward.getId());

		/* Save the newOutward in the database */
		newOutward = outwardRegisterService.insert(newOutward);

		return ResponseEntity.status(HttpStatus.OK).body(newOutward);
	}

	/*
	 * PATH: /outwardregister/delete/{outwardId} Method: DELETE RequestBody: None
	 * response: String PathVariable: outwardId
	 */
	@DeleteMapping("/delete/{outwardId}")
	public ResponseEntity<?> deleteOutward(@PathVariable("outwardId") int outwardId) {

		/* validate the Id and fetch the object to be deleted */
		OutwardRegister outward = outwardRegisterService.getOutward(outwardId);
		if (outward == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Invalid outwardId given. Try again with correct Id");
		}

		/* delete the outward from the database */
		outwardRegisterService.deleteOutward(outward);
		return ResponseEntity.status(HttpStatus.OK).body("outward deleted Successfully!!");
	}

	/*
	 * PATH: /outwardregister/report Method: GET RequestBody: None response:
	 * List<OutwardRegisterDto> PathVariable: None
	 */
	@GetMapping("/report")
	public List<OutwardRegisterDto> outwardReport() {
		/* fetch all the records of OutwardRegister */
		List<OutwardRegister> list = outwardRegisterService.getAllOutwards();
		List<OutwardRegisterDto> listDto = new ArrayList<>();

		/*
		 * Iterate through the records of OutwardRegister list and assign the required
		 * values to outwardRegisterDto
		 */
		list.stream().forEach(item -> {
			OutwardRegisterDto outwardRegisterdto = new OutwardRegisterDto();
			outwardRegisterdto.setProductTitle(item.getProduct().getTitle());
			outwardRegisterdto.setProductQuantity(item.getQuantity());
			outwardRegisterdto.setGodownLocation(item.getGodown().getLocation());
			outwardRegisterdto.setGodownManager(item.getGodown().getManager().getName());
			outwardRegisterdto.setRecieptNo(item.getReceiptNo());
			listDto.add(outwardRegisterdto);
		});

		return listDto;
	}

	// Returning a map which shows the distinct products purchased by a particular
	// customer along with their total purchased quantities
	// HashMap<ProductName, totalQuantityPurchased> by a specific customer
	/*
	 * PATH: /outwardregister/report/customer/{customerId} Method: GET RequestBody:
	 * None response: HashMap<ProductName, totalQuantityPurchased> PathVariable:
	 * customerId
	 */
	@GetMapping("/report/customer/{customerId}")
	public ResponseEntity<?> outwardReportByCustomer(@PathVariable int customerId) {

		/* Fetch the customerProduct records with the given customerId if it is valid */
		List<CustomerProduct> customerProductList = customerProductService.getByCustomerId(customerId);
		// Checking based on the size of list
		if (customerProductList.size() < 1) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid customer ID given");
		}

		/*
		 * Create a HashMap with key as productName and value as total number of
		 * products purchased by him
		 */
		/*
		 * Iterate through the customerProductList and Insert or increment the count
		 * based on the presence of key
		 */
		HashMap<String, Integer> map = new HashMap<>();
		customerProductList.stream().forEach(item -> {
			// Checking if the key already exists and incrementing count else inserting
			// key,value
			if (map.containsKey(item.getProduct().getTitle())) {
				map.put(item.getProduct().getTitle(),
						map.get(item.getProduct().getTitle()) + item.getQuantityPurchased());
			} else {
				map.put(item.getProduct().getTitle(), item.getQuantityPurchased());
			}
		});
		return ResponseEntity.status(HttpStatus.OK).body(map);
	}

	// Returning a map which shows all the customers along with their total
	// purchased item quantities for all products
	// HashMap<CustomerName, totalQuantityPurchased> by a specific customer
	/*
	 * PATH: /outwardregister/report/customer Method: GET RequestBody: None
	 * response: HashMap<CustomerName, totalQuantityPurchased> PathVariable: none
	 */
	@GetMapping("/report/customer")
	public ResponseEntity<?> getCustomerReport() {

		/* Fetch the customerProductList */
		List<CustomerProduct> customerProductList = customerProductService.getAllCustomerProducts();

		/*
		 * Create a HashMap with key as customerName and value as total number of
		 * products purchased by him
		 */
		/*
		 * Iterate through the customerProductList and Insert or increment the count
		 * based on the presence of key
		 */
		HashMap<String, Integer> customerMap = new HashMap<>();
		customerProductList.stream().forEach(item -> {
			Customer customer = item.getCustomer();
			// Checking if the key already exists and incrementing count else inserting
			// key,value
			if (customerMap.containsKey(customer.getName())) {
				int count = customerMap.get(customer.getName());
				customerMap.put(customer.getName(), count + item.getQuantityPurchased());
			} else {
				customerMap.put(customer.getName(), item.getQuantityPurchased());
			}
		});

		return ResponseEntity.status(HttpStatus.OK).body(customerMap);
	}

}
