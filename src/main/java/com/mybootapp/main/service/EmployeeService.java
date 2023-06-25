package com.mybootapp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.Employee;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.repository.EmployeeRespository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRespository employeeRepository;
	
	// To save the object in the database we use JpaRepository method save()
	public Employee insert(Employee employee) {
		return employeeRepository.save(employee);
	}
	
	// To get all records from database we use JpaRepository method findAll()
	public List<Employee> getAllEmployees() {
		return employeeRepository.findAll();
	}

	// To get one record with particular Id from db we use JpaRepository method findById()
	public Employee getemployeeById(int employeeId) {
		Optional<Employee> optional = employeeRepository.findById(employeeId);
		if(!optional.isPresent()) {
			return null; 
		}
		return optional.get();
	}

	// To delete a certain record from database we use JpaRepository method delete()
	public void deleteProduct(Employee employee) {
		employeeRepository.delete(employee);		
	}

}
