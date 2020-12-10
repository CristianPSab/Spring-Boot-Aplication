package com.ex.Aplication.service;


import com.ex.Aplication.entity.User;

public interface UserService {
	
	public Iterable<User> getAllUsers();

	public User createUser(User user) throws Exception;

}
