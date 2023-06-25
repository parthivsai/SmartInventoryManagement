package com.mybootapp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.OutwardRegister;
import com.mybootapp.main.repository.OutwardRegisterRepository;

@Service
public class OutwardRegisterService {

	@Autowired
	private OutwardRegisterRepository outwardRegisterRepository;
	
	// To save the object in the database we use JpaRepository method save()
	public OutwardRegister insert(OutwardRegister outwardRegister) {
		return outwardRegisterRepository.save(outwardRegister);
	}

	// To get all records from database we use JpaRepository method findAll()
	public List<OutwardRegister> getAllOutwards() {
		return outwardRegisterRepository.findAll();
	}

	// To get one record with particular Id from db we use JpaRepository method findById()
	public OutwardRegister getOutward(int outwardId) {
		Optional<OutwardRegister> optional =  outwardRegisterRepository.findById(outwardId);
		if(!optional.isPresent()) {
			return null;
		}
		return optional.get();
	}

	// To delete a certain record from database we use JpaRepository method delete()
	public void deleteOutward(OutwardRegister outward) {
		outwardRegisterRepository.delete(outward);
	}

}
