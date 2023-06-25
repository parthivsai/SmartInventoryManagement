package com.mybootapp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.Godown;
import com.mybootapp.main.model.Product;
import com.mybootapp.main.repository.GodownRepository;

@Service
public class GodownService {

	@Autowired
	private GodownRepository godownRepository; 
	
	// To save the object in the database we use JpaRepository method save()
	public Godown insert(Godown godown) {
		return godownRepository.save(godown);
	}

	// To get one record with particular Id from db we use JpaRepository method findById()
	public Godown getById(int godownId) {
		Optional<Godown> optional= godownRepository.findById(godownId);
		if(!optional.isPresent()) {
			return null; 
		}
		return optional.get();
	}

	// To get all records from database we use JpaRepository method findAll()
	public List<Godown> getAllGodowns() {
		return godownRepository.findAll();
	}

	// To delete a certain record from database we use JpaRepository method delete()
	public void deleteGodown(Godown godown) {
		godownRepository.delete(godown);
	}

}