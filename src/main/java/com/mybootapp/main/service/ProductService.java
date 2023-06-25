package com.mybootapp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.exception.ResourceNotFound;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	// To save the object in the database we use JpaRepository method save()
	public Product insert(Product product) {
		return productRepository.save(product);	 
	}

	// To get all records from database we use JpaRepository method findAll()
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	public Product getproduct(int id) {
		//If id is valid we get the product 
		//If id is not valid: we get null 
		Optional<Product> optional = productRepository.findById(id);
		if(!optional.isPresent()) {
			return null; 
		}
		return optional.get();
	}

	public Product getproductAlternate(int id) throws ResourceNotFound{
		Optional<Product> optional= productRepository.findById(id);
		if(!optional.isPresent()) {
			throw new ResourceNotFound("Invalid ID Given.."); 
		}
		return optional.get();
	}

	// To delete a certain record from database we use JpaRepository method delete()
	public void deleteProduct(Product product) {
		productRepository.delete(product);
	}

	// To get one record with particular Id from db we use JpaRepository method findById()
	public Product getById(int productId) {
		Optional<Product> optional= productRepository.findById(productId);
		if(!optional.isPresent()) {
			return null; 
		}
		return optional.get();
	}

}