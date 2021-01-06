package com.ex.Aplication.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ex.Aplication.Exception.CustomFieldValidationException;
import com.ex.Aplication.Exception.UserNameDoNotFound;
import com.ex.Aplication.dto.ChangePasswordForm;
import com.ex.Aplication.entity.User;
import com.ex.Aplication.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository repository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	
	@Override
	public Iterable<User> getAllUsers() {
		return repository.findAll();
	}
	
	private boolean checkUsernameAvailable(User user) throws Exception {
		
		Optional<User> userFound = repository.findByUsername(user.getUsername());
		if(userFound.isPresent()) {
			throw new CustomFieldValidationException("Username no disponible", "username");
		}
		return true;
	}
	private boolean checkPasswordValid(User user) throws Exception {
		if (user.getConfirmPassword() == null || user.getConfirmPassword().isEmpty()) {
			throw new CustomFieldValidationException ("Confirm Password es obligatorio", "confirmPassword");
		}
		
		if(!user.getPassword().equals(user.getConfirmPassword())) {
			throw new CustomFieldValidationException("Password y Confirm Password no son iguales", "password");
		}
		return true;
	}

	@Override
	public User createUser(User user) throws Exception {
		if (checkUsernameAvailable(user) && checkPasswordValid(user)) {
			String encodePassword = bCryptPasswordEncoder.encode(user.getPassword());
			user.setPassword(encodePassword);
			user = repository.save(user);
		}
		return user;
	}

	@Override
	public User getUserById(Long id) throws UserNameDoNotFound {
		return repository.findById(id).orElseThrow(() -> new  UserNameDoNotFound("El Id del usuario no existe"));
	}

	@Override
	public User updateUser(User fromuser) throws Exception {
		User toUser = getUserById(fromuser.getId());
		mapUser(fromuser, toUser);
		return repository.save(toUser);
	}
	
	/**
	 * Map everything but the password
	 * @param from
	 * @param to
	 */
	protected void mapUser(User from, User to) {
		to.setUsername(from.getUsername());
		to.setFirstName(from.getFirstName());
		to.setLastName(from.getLastName());
		to.setEmail(from.getEmail());
		to.setRoles(from.getRoles());
		to.setPassword(from.getPassword());
		
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	public void deleteUser(Long id) throws UserNameDoNotFound {		
		User user = getUserById(id);
		repository.delete(user);		
	}

	@Override
	public User changePassword(ChangePasswordForm form) throws Exception {
		User user = getUserById(form.getId());
		
		if(!isLoggedUserADMIN() && !user.getPassword().equals(form.getCurrentPassword())) {
			throw new Exception ("Current Password invalido");
			
		}
		
		if(user.getPassword().equals(form.getNewPassword())) {
			throw new Exception ("El nuevo Password debe ser diferente al password actual.");
		}
		
		if(!form.getNewPassword().equals(form.getConfirmPassword())) {
			throw new Exception ("El nuevo Password y el Current Password no coinciden.");
		
		}
		
		String encodePassword = bCryptPasswordEncoder.encode(form.getNewPassword());
		user.setPassword(encodePassword);
		user = repository.save(user);
	
		return 	repository.save(user);
	}
	
	private boolean isLoggedUserADMIN() {
		//Obtener el usuario logeado
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		UserDetails loggedUser = null;
		Object roles = null;

		//Verificar que ese objeto traido de sesion es el usuario
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;

			roles = loggedUser.getAuthorities().stream()
					.filter(x -> "ROLE_ADMIN".equals(x.getAuthority())).findFirst()
					.orElse(null); 
		}
		return roles != null ? true : false;
	}
	
	private User getLoggedUser() throws Exception {
		//Obtener el usuario logeado
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		UserDetails loggedUser = null;

		//Verificar que ese objeto traido de sesion es el usuario
		if (principal instanceof UserDetails) {
			loggedUser = (UserDetails) principal;
		}
		
		User myUser = repository
				.findByUsername(loggedUser.getUsername()).orElseThrow(() -> new Exception("Problemas obteniendo usuario de sesión."));
		
		return myUser;
	}
	
}
