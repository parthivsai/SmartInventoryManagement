package com.mybootapp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.Customer;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository; 

	// To save the object in the database we use JpaRepository method save()
	public Customer insert(Customer customer) {
		return customerRepository.save(customer);
	}

	// To get one record with particular Id from db we use JpaRepository method findById()
	public Customer getById(int customerId) {
		Optional<Customer> optional= customerRepository.findById(customerId);
		if(!optional.isPresent()) {
			return null; 
		}
		return optional.get();
	}
	
	// To get all records from database we use JpaRepository method findAll()
	public List<Customer> getCustomers() {
		return customerRepository.findAll();
	}
	
	// To delete a certain record from database we use JpaRepository method delete()
	public void deleteCustomer(Customer customer) {
		customerRepository.delete(customer);	
	}

}
