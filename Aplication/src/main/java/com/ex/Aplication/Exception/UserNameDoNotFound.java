package com.ex.Aplication.Exception;

public class UserNameDoNotFound extends Exception {
		
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -8513658604636114921L;

	public UserNameDoNotFound() {
			super("Usuario o Id no encontrado");
	}
	
	public UserNameDoNotFound(String message) {
		super(message);
	}
}
