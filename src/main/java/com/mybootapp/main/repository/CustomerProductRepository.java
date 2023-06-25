package com.mybootapp.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mybootapp.main.model.CustomerProduct;

public interface CustomerProductRepository extends JpaRepository<CustomerProduct, Integer> {
	
	// fetch only the records with the given customerId 
	@Query("select cp from CustomerProduct cp where cp.customer.id=?1")
	List<CustomerProduct> getByCustomerId(int customerId);

}
