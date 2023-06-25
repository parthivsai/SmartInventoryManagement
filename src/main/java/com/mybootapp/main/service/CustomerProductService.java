package com.mybootapp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.CustomerProduct;
import com.mybootapp.main.repository.CustomerProductRepository;

@Service
public class CustomerProductService {

	@Autowired
	private CustomerProductRepository customerProductRepository;

	// To save the object in the database we use JpaRepository method save()
	public CustomerProduct insert(CustomerProduct customerProduct) {
		return customerProductRepository.save(customerProduct);
	}

	// To get all records from database we use JpaRepository method findAll()
	public List<CustomerProduct> getAllCustomerProducts() {
		return customerProductRepository.findAll();
	}

	// To get one record with particular Id from db we use JpaRepository method findById()
	public CustomerProduct getById(int id) {
		Optional<CustomerProduct> optional = customerProductRepository.findById(id);

		if (!optional.isPresent()) {
			return null;
		}

		return optional.get();
	}

	// To delete a certain record from database we use JpaRepository method delete()
	public void deleteManager(CustomerProduct customerProduct) {
		customerProductRepository.delete(customerProduct);
	}

	public List<CustomerProduct> getByCustomerId(int customerId) {
		return customerProductRepository.getByCustomerId(customerId);
	}

}
