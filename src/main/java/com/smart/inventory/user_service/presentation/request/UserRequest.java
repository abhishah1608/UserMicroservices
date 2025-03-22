package com.smart.inventory.user_service.presentation.request;

import java.time.LocalDateTime;

import com.smart.inventory.user_service.application.validation.annotation.ValidUserRegistration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@ValidUserRegistration
public class UserRequest {
	
	@NotBlank(message = "Username Field should be required.")
    private String username;
	
	@NotBlank(message = "Password Field is required.")
	private String password;
	
	@NotBlank(message = "Role Field is required.")
    private String role;
    
    @NotBlank(message = "Email Address Field is required.")
    @Email(message = "Please provide the Correct Email address format.")
    private String email;
   
    private LocalDateTime createdAt;
    
    private LocalDateTime validTo;
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getValidTo() {
		return validTo;
	}
	public void setValidTo(LocalDateTime validTo) {
		this.validTo = validTo;
	}
    
    

}
