package com.mybootapp.main.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.Manager;
import com.mybootapp.main.repository.ManagerRepository;

@Service
public class ManagerService {

	@Autowired
	private ManagerRepository managerRepository;
	
	// To save the object in the database we use JpaRepository method save()
	public Manager insert(Manager manager) {
		return managerRepository.save(manager);
	}

	// To get one record with particular Id from db we use JpaRepository method findById()
	public Manager getById(int managerID) {
		Optional<Manager> optional = managerRepository.findById(managerID);
		
		if(!optional.isPresent())
			return null;
		
		return optional.get(); //returns manager
	}

	// To get all records from database we use JpaRepository method findAll()
	public List<Manager> getAllManagers() {
		return managerRepository.findAll();
	}

	// To delete a certain record from database we use JpaRepository method delete()
	public void deleteManager(Manager manager) {
		managerRepository.delete(manager);
	}

}
