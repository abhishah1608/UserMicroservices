package com.smart.inventory.user_service.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart.inventory.user_service.application.service.UserService;
import com.smart.inventory.user_service.presentation.dto.ErrorResponse;
import com.smart.inventory.user_service.presentation.dto.UserDto;
import com.smart.inventory.user_service.presentation.request.UserRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {

	UserService userservice = null;

	public UserController(UserService userservice) {
		this.userservice = userservice;
	}

	
	@PostMapping("/createuser")
	public ResponseEntity<?> createUser(@Validated @RequestBody UserRequest user) {
		UserDto userdto = null;
		try {
			userdto = this.userservice.createUser(user);
		} catch (Exception ex) {
	        ErrorResponse error = new ErrorResponse("Server side error", ex.getMessage());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		} 
		return ResponseEntity.ok(userdto);
	}

}
