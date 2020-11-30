package com.ex.Aplication.repository;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import com.ex.Aplication.entity.User;

public interface UserRepository extends CrudRepository <User, Long> {

	public Set <User> findByUsername (String username);
}
