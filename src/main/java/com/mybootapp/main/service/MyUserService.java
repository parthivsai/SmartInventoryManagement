package com.mybootapp.main.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mybootapp.main.model.Godown;
import com.mybootapp.main.model.User;
import com.mybootapp.main.repository.UserRepository;

@Service
public class MyUserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	// To save the object in the database we use JpaRepository method save()
	public User insert(User user) {
		return userRepository.save(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.getUserByUsername(username);
		return user;
	}

	// To get one record with particular Id from db we use JpaRepository method findById()
	public User getById(int userId) {
		Optional<User> optional= userRepository.findById(userId);
		if(!optional.isPresent()) {
			return null; 
		}
		return optional.get();
	}

	// To delete a certain record from database we use JpaRepository method delete()
	public void deleteUser(User user) {
		userRepository.delete(user);
	}

}
