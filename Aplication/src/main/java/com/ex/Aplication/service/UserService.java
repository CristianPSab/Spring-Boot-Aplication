package com.ex.Aplication.service;


import com.ex.Aplication.Exception.UserNameDoNotFound;
import com.ex.Aplication.dto.ChangePasswordForm;
import com.ex.Aplication.entity.User;

public interface UserService {
	
	public Iterable<User> getAllUsers();

	public User createUser(User user) throws Exception;
	
	public User getUserById(Long id)throws Exception;
	
	public User updateUser(User user)throws Exception;
	
	public void deleteUser(Long id) throws UserNameDoNotFound;
	
	public User changePassword(ChangePasswordForm form) throws Exception;

}
