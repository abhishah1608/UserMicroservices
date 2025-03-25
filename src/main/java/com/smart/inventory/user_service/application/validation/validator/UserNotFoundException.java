package com.smart.inventory.user_service.application.validation.validator;

public class UserNotFoundException extends RuntimeException {
	
	public UserNotFoundException(String message)  {
        super(message);
    }
	
}
