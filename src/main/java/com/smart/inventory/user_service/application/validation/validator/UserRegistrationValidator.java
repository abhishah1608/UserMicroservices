package com.smart.inventory.user_service.application.validation.validator;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smart.inventory.user_service.application.validation.annotation.ValidUserRegistration;
import com.smart.inventory.user_service.domain.repository.UserRepository;
import com.smart.inventory.user_service.presentation.request.UserRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UserRegistrationValidator implements ConstraintValidator<ValidUserRegistration, UserRequest> {

    @Autowired
    private UserRepository userRepository;  // DB access

    @Override
    public boolean isValid(UserRequest userRequest, ConstraintValidatorContext context) {
        // Check if combination exists in DB
        boolean exists = false;
		try {
			exists = userRepository.existsByUsernameAndEmail(
			        userRequest.getUsername(), userRequest.getEmail());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (exists) {
            // Custom message for specific fields
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Username or email are already registered")
                   .addPropertyNode("username")
                   .addConstraintViolation();
            System.out.println("Violation of the username and Email");	
            return false;
        }

        return true;
    }
}

