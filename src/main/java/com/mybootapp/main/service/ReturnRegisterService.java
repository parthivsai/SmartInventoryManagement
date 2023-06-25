package com.mybootapp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.OutwardRegister;
import com.mybootapp.main.model.ReturnRegister;
import com.mybootapp.main.repository.ReturnRegisterRepository;

@Service
public class ReturnRegisterService {
	
	@Autowired
	private ReturnRegisterRepository returnRegisterRepository;

	// To save the object in the database we use JpaRepository method save()
	public ReturnRegister insert(ReturnRegister returnRegister) {
		return returnRegisterRepository.save(returnRegister);
	}

	// To get all records from database we use JpaRepository method findAll()
	public List<ReturnRegister> getAllReturns() {
		return returnRegisterRepository.findAll();
	}

	// To get one record with particular Id from db we use JpaRepository method findById()
	public ReturnRegister getById(int returnId) {
		Optional<ReturnRegister> optional =  returnRegisterRepository.findById(returnId);
		if(!optional.isPresent()) {
			return null;
		}
		return optional.get();
	}

	// To delete a certain record from database we use JpaRepository method delete()
	public void deleteReturn(ReturnRegister returnRecord) {
		returnRegisterRepository.delete(returnRecord);
	}

}
