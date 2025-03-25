package com.smart.inventory.user_service.application.validation.validator;

public class UnauthorizedException extends RuntimeException {
 
	public UnauthorizedException(String message)  {
        super(message);
    }
}
