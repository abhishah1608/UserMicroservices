package com.smart.inventory.user_service.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smart.inventory.user_service.application.service.UserService;
import com.smart.inventory.user_service.application.validation.annotation.JwtAuthRequired;
import com.smart.inventory.user_service.application.validation.validator.UserNotFoundException;
import com.smart.inventory.user_service.infrastructure.repository.JwtService;
import com.smart.inventory.user_service.presentation.dto.ErrorResponse;
import com.smart.inventory.user_service.presentation.dto.UserDto;
import com.smart.inventory.user_service.presentation.request.UserRequest;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin(origins = {"http://localhost:3000","http://localhost:3000"}, methods = {RequestMethod.GET, RequestMethod.POST})
public class UserController {

	UserService userservice = null;
	
    private JwtService jwtService;

	public UserController(UserService userservice, JwtService jwtService) {
		this.userservice = userservice;
		this.jwtService = jwtService;
	}

	
	@PostMapping("/createuser")
	public ResponseEntity<?> createUser(@Validated @RequestBody UserRequest user) {
		UserDto userdto = null;
		try {
			userdto = this.userservice.createUser(user);
		} catch (Exception ex) {
	        ErrorResponse error = new ErrorResponse("Server side error", ex.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		} 
		return ResponseEntity.ok(userdto);
	}
	
	@GetMapping("/auth")
	public ResponseEntity<?> getUser(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse response)
	{
		UserDto userdto = null;
		try {
			userdto = this.userservice.getUser(username, password);
	        if(userdto == null)
	        {
	        	throw new UserNotFoundException("No user found with given credentials.");
	        }
	        else {
	        	String token = jwtService.generateToken(userdto);
	            response.setHeader("token", token);
	        }
		} catch (Exception ex) {
	        ErrorResponse error = new ErrorResponse("Server side error", ex.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
		return ResponseEntity.ok(userdto);
	}
	
	@GetMapping("/test")
	@JwtAuthRequired
	public ResponseEntity<?> allowAccess()
	{
		return ResponseEntity.ok("Allowed to access resourse");
	}

}
