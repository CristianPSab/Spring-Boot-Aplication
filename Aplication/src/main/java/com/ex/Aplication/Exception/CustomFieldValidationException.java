package com.ex.Aplication.Exception;

public class CustomFieldValidationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 58264848234797964L;

	private String fieldName;
	
	
	public CustomFieldValidationException(String message, String fieldName) {
		super(message);
		this.fieldName = fieldName;
	}
	
	public String getFieldName() {
		return this.fieldName;
	}
	
}
