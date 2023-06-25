package com.mybootapp.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mybootapp.main.model.Product;

// leverage the functionality of spring JPA
public interface ProductRepository extends JpaRepository<Product, Integer> {
		
	// All the methods of JpaRepository are now available in ProductRepository
	/* useful Methods
	 * save(T):T - saves the object in DB
	 * getAll(): List<T> - returns all the objects present in DB
	 * findById(id): T - returns the object on basis of ID
	 *  
	 * 
	 */
	
}
