package com.mybootapp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.InwardRegister;
import com.mybootapp.main.repository.InwardRegisterRepository;

@Service
public class InwardRegisterService {

	@Autowired
	private InwardRegisterRepository inwardRegisterRepository;
	
	// To save the object in the database we use JpaRepository method save()
	public InwardRegister insert(InwardRegister inwardRegister) {
		return inwardRegisterRepository.save(inwardRegister);
	}
	
	public boolean checkQuantity(int productId, int quantityPuchased) {
		InwardRegister inwardRegister = inwardRegisterRepository
				.checkQuantity(productId,quantityPuchased);
		if(inwardRegister == null)
			return false;
		return true;
	}
	
	// To get all records from database we use JpaRepository method findAll()
	public List<InwardRegister> getAllInwards() {
		return inwardRegisterRepository.findAll();
	}
	
	// To get one record with particular Id from db we use JpaRepository method findById()
	public InwardRegister getById(int id) {
		Optional<InwardRegister> optional = inwardRegisterRepository.findById(id);
		if(!optional.isPresent()) {
			return null;
		}
		return optional.get();
	}
	
	// To delete a certain record from database we use JpaRepository method delete()
	public void deleteManager(InwardRegister inward) {
		inwardRegisterRepository.delete(inward);
	}
	
	public List<InwardRegister> getBySupplier(int supplierId) {
		return inwardRegisterRepository.getBySupplier(supplierId);
	}
	
}