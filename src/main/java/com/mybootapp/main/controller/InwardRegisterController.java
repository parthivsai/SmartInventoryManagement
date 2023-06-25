package com.mybootapp.main.controller;

import java.time.LocalDate;
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

import com.mybootapp.main.Dto.InwardRegisterDto;
import com.mybootapp.main.Dto.InwardRegisterBySupplierDto;
import com.mybootapp.main.model.Godown;
import com.mybootapp.main.model.InwardRegister;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.model.Supplier;
import com.mybootapp.main.service.GodownService;
import com.mybootapp.main.service.InwardRegisterService;
import com.mybootapp.main.service.ProductService;
import com.mybootapp.main.service.SupplierService;

@RestController
@RequestMapping("/inwardregister")
public class InwardRegisterController {

	// If we don't use Autowired we will get null pointer exception. 
	// SpringBoot automatically autowires it for us.
	@Autowired
	private ProductService productService;

	@Autowired
	private GodownService godownService;

	@Autowired
	private SupplierService supplierService;

	@Autowired
	private InwardRegisterService inwardRegisterService;

	/* 
	 PATH: /inwardregister/add/{productId}/{godownId}/{supplierId}
	 Method: POST
	 RequestBody: InwardRegister inwardRegister
	 response: inwardRegister 
	 PathVariable: productId, godownId, supplierId
	 */
	@PostMapping("/add/{productId}/{godownId}/{supplierId}")
	public ResponseEntity<?> postInwardRegister(@RequestBody InwardRegister inwardRegister,
			@PathVariable("productId") int productId, @PathVariable("godownId") int godownId,
			@PathVariable("supplierId") int supplierId) {

		/* Validate Ids and fetch Objects */
		Product product = productService.getById(productId);
		if (product == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product ID given");
		Godown godown = godownService.getById(godownId);
		if (godown == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid godown ID given");
		Supplier supplier = supplierService.getById(supplierId);
		if (supplier == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid supplier ID given");

		/* Attach all objects to inwardRegister */
		inwardRegister.setProduct(product);
		inwardRegister.setGodown(godown);
		inwardRegister.setSupplier(supplier);

		inwardRegister.setDateOfSupply(LocalDate.now());

		/* save inwardRegister object */
		inwardRegister = inwardRegisterService.insert(inwardRegister);
		return ResponseEntity.status(HttpStatus.OK).body(inwardRegister);
	}
	
	/* 
	 PATH: /inwardregister/all
	 Method: GET
	 RequestBody: None
	 response: List<InwardRegister> 
	 PathVariable: None
	 */
	@GetMapping("/all")
	public List<InwardRegister> getInwards() {
		/* fetch all inwards and return them as a list */
		List<InwardRegister> inwards = inwardRegisterService.getAllInwards();
		return inwards;
	}

	/* 
	 PATH: /inwardregister/one/{id}
	 Method: GET
	 RequestBody: None
	 response: InwardRegister 
	 PathVariable: id
	 */
	@GetMapping("/one/{id}")
	public ResponseEntity<?> getOneManager(@PathVariable("id") int id) {
		/* check if the given id is valid and return inward */
		InwardRegister inward = inwardRegisterService.getById(id);
		if (inward == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid InwardRegisterID!!");
		}

		return ResponseEntity.status(HttpStatus.OK).body(inward);
	}

	/* 
	 PATH: /inwardregister/update/{id}/{productId}/{godownId}/{supplierId}
	 Method: PUT
	 RequestBody: InwardRegister newInwardRegister
	 response: newInwardRegister 
	 PathVariable: id, productId, godownId, supplierId
	 */
	@PutMapping("/update/{id}/{productId}/{godownId}/{supplierId}")
	public ResponseEntity<?> updateManager(@PathVariable("id") int id,
			@PathVariable("productId") int productId,
			@PathVariable("godownId") int godownId,
			@PathVariable("supplierId") int supplierId,
			@RequestBody InwardRegister newInward) {

		/* Validate Ids and fetch Objects */
		InwardRegister oldInward = inwardRegisterService.getById(id);
		if (oldInward == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid InwardID given!!");
		}
		Product product = productService.getById(productId);
		if (product == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid product ID given");
		Godown godown = godownService.getById(godownId);
		if (godown == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid godown ID given");
		Supplier supplier = supplierService.getById(supplierId);
		if (supplier == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid supplier ID given");
		
		/* Attach all objects to newInward */
		newInward.setId(oldInward.getId());
		newInward.setProduct(product);
		newInward.setSupplier(supplier);
		newInward.setGodown(godown);
		newInward.setDateOfSupply(oldInward.getDateOfSupply());

		/* Save it in the database */
		newInward = inwardRegisterService.insert(newInward);

		return ResponseEntity.status(HttpStatus.OK).body(newInward);
	}

	/* 
	 PATH: /inwardregister/delete/{id}
	 Method: DELETE
	 RequestBody: None
	 response: String 
	 PathVariable: id
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteInward(@PathVariable("id") int id) {
		/* check if the id is valid */
		InwardRegister Inward = inwardRegisterService.getById(id);
		if (Inward == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid inwardID given!!");
		}
		
		/* delete the record from the database */
		inwardRegisterService.deleteManager(Inward);
		return ResponseEntity.status(HttpStatus.OK).body("Successfully Deleted!");
	}
	
	/* 
	 PATH: /inwardregister/report
	 Method: GET
	 RequestBody: None
	 response: List<InwardRegisterDto> 
	 PathVariable: None
	 */
	@GetMapping("/report")
	public List<InwardRegisterDto> inwardReport() {
		/* fetch all the records of InwardRegister*/
		List<InwardRegister> list = inwardRegisterService.getAllInwards();	
		List<InwardRegisterDto> listDto = new ArrayList<InwardRegisterDto>();
		
		/* Iterate through the records of InwardRegister list and assign the required values to inwardRegisterDto */
		list.stream().forEach(entry->{
			InwardRegisterDto inwardRegisterDto = new InwardRegisterDto();
			inwardRegisterDto.setProductTitle(entry.getProduct().getTitle());
			inwardRegisterDto.setProductQuantity(entry.getQuantity());
			inwardRegisterDto.setGodownLocation(entry.getGodown().getLocation());
			inwardRegisterDto.setGodownManager(entry.getGodown().getManager().getName());
			inwardRegisterDto.setSupplierName(entry.getSupplier().getName());
			inwardRegisterDto.setSupplierCity(entry.getSupplier().getCity());
			listDto.add(inwardRegisterDto);
		});
		return listDto;
	}
	
	/* 
	 PATH: /inwardregister/report/supplier/{supplierId}
	 Method: GET
	 RequestBody: None
	 response: List<InwardRegisterDto> 
	 PathVariable: supplierId
	 */
	@GetMapping("/report/supplier/{supplierId}")
	public ResponseEntity<?> getProductsBySupplierReport(@PathVariable("supplierId") int supplierId){
		
		/* Check if the given supplierId is valid */
		Supplier supplier = supplierService.getById(supplierId);
		if(supplier == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid SupplierId.. try again with correct Id!");
		}
		
		/* fetch the records with the given supplierId*/
		List<InwardRegister> list = inwardRegisterService.getBySupplier(supplierId);
		
		/* Iterate through the records of InwardRegister list and assign the required values to ProductBySupplierDto */
		List<InwardRegisterBySupplierDto> listDto = new ArrayList<>();
		list.stream().forEach(item -> {
			InwardRegisterBySupplierDto productBySupplier = new InwardRegisterBySupplierDto();
			productBySupplier.setProductTitle(item.getProduct().getTitle());
			productBySupplier.setProductQuantity(item.getQuantity());
			productBySupplier.setProductPrice(item.getProduct().getPrice());
			productBySupplier.setSupplierName(item.getSupplier().getName());
			productBySupplier.setSupplierCity(item.getSupplier().getCity());
			listDto.add(productBySupplier);
		});
		return ResponseEntity.status(HttpStatus.OK).body(listDto);
	}

}