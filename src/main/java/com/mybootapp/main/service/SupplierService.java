package com.mybootapp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.Product;
import com.mybootapp.main.model.Supplier;
import com.mybootapp.main.repository.SupplierRepository;

@Service
public class SupplierService {

	@Autowired
	private SupplierRepository supplierRepository; 
	
	// To save the object in the database we use JpaRepository method save()
	public Supplier insert(Supplier supplier) {
		return supplierRepository.save(supplier);
	}
	
	// To get one record with particular Id from db we use JpaRepository method findById()
	public Supplier getById(int supplierId) {
		Optional<Supplier> optional= supplierRepository.findById(supplierId);
		if(!optional.isPresent()) {
			return null; 
		}
		return optional.get();	}

	// To get all records from database we use JpaRepository method findAll()
	public List<Supplier> getSuppliers() {
		return supplierRepository.findAll();
	}

	// To delete a certain record from database we use JpaRepository method delete()
	public void deleteSupplier(Supplier supplier) {
		supplierRepository.delete(supplier);
	}

}