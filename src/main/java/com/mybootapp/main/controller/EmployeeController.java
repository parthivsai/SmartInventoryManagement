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

import com.mybootapp.main.model.Employee;
import com.mybootapp.main.model.Godown;
import com.mybootapp.main.model.Manager;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.model.User;
import com.mybootapp.main.service.EmployeeService;
import com.mybootapp.main.service.ManagerService;
import com.mybootapp.main.service.MyUserService;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	// If we don't use Autowired we will get null pointer exception. 
	// Spring automatically autowires for us.
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private MyUserService userService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private ManagerService managerService; 

	/* 
	 PATH: /employee/add/{managerId}
	 Method: POST
	 RequestBody: Employee employee
	 response: employee 
	 PathVariable: managerId
	 */
	@PostMapping("/add/{managerId}")
	public ResponseEntity<?> addEmployee(@PathVariable("managerId") int managerId, 
			@RequestBody Employee employee) {
		
		/* validate managerId and fetch manager obj from DB */
		Manager manager  = managerService.getById(managerId);
		if(manager == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Manager ID invalid");

		/* attach manager to employee */
		employee.setManager(manager);

		/* Fetch the user from employee */
		User user = employee.getUser();

		/* Encode the password given as Plain text from UI */
		user.setPassword(encoder.encode(user.getPassword()));

		/* Set the role: EMPLOYEE */
		user.setRole("EMPLOYEE");

		/* Save the user in DB */
		user  = userService.insert(user);

		/* Attach user to employee and save employee */
		employee.setUser(user);
		employee =  employeeService.insert(employee);
		return ResponseEntity.status(HttpStatus.OK).body(employee);
	}
	
	
	/* 
	 PATH: /employee/all
	 Method: GET
	 RequestBody: None
	 response: List<Employee> 
	 PathVariable: None
	 */
	@GetMapping("/all")
	public List<Employee> getAllEmployees() {
		/* Get all employees and return the list */
		List<Employee> list =  employeeService.getAllEmployees();
		return list; 
	}
	
	/* 
	 PATH: /employee/one/{employeeId}
	 Method: GET
	 RequestBody: None
	 response: employee 
	 PathVariable: employeeId
	 */
	@GetMapping("/one/{employeeId}") //this id is called as path variable
	public ResponseEntity<?> getEmployee(@PathVariable("employeeId") int employeeId) {
		
		/* Validate the given employee Id and return the employee if Id is valid */
		Employee employee  = employeeService.getemployeeById(employeeId);
		if(employee == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Employee ID given");
		}
		return ResponseEntity.status(HttpStatus.OK).body(employee); 
	}
	
	/* 
	 PATH: /employee/update/{employeeId}/{managerId}
	 Method: PUT
	 RequestBody: Employee newEmployee
	 response: Employee 
	 PathVariable: employeeId
	 */
	@PutMapping("/update/{employeeId}/{managerId}")
	public ResponseEntity<?> updateEmployee(@PathVariable("employeeId") int employeeId,
			@PathVariable("managerId") int managerId,
			@RequestBody Employee newEmployee){
		
		/* validate and fetch objects based on given id's */
		Employee oldEmployee = employeeService.getemployeeById(employeeId);
		
		if(oldEmployee == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid EmployeeId given!!");
		}
		
		/* Validate and fetch Manager from managerId */
		Manager manager = managerService.getById(managerId);
		if(manager == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Manager ID");
		}
		
		newEmployee.setId(oldEmployee.getId());
		
		User user = newEmployee.getUser();

		/* Encode the password given as Plain text from UI */
		user.setPassword(encoder.encode(user.getPassword()));

		/* Set the role: EMPLOYEE */
		user.setRole("EMPLOYEE");

		/* Save the user in DB */
		user  = userService.insert(user);

		/* Attach user to employee and save employee */
		newEmployee.setUser(user);
		
		newEmployee.setManager(manager);
		
		newEmployee = employeeService.insert(newEmployee);

		return ResponseEntity.status(HttpStatus.OK).body(newEmployee);
	}
	
	/* 
	 PATH: /employee/delete/{id}
	 Method: DELETE
	 RequestBody: None
	 response: String 
	 PathVariable: id
	 */
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable("id") int id) {
		/* validate id and get the object */
		Employee employee  = employeeService.getemployeeById(id);
		if(employee == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Employee ID given");
		}
		
		/* delete the record from the database */
		employeeService.deleteProduct(employee);
		return ResponseEntity.status(HttpStatus.OK).body("Employee deleted..");

	}
}
