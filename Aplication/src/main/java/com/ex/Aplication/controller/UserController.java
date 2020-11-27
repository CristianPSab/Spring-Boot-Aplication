package com.ex.Aplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

	@GetMapping("/")
	public String index() {
		return "Index";
	}
	
	@GetMapping("/userForm") 
	public String userForm() {
		return "user-form/user-view";
	}
}
