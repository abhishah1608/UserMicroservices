package com.smart.inventory.user_service.domain.repository;

import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import com.smart.inventory.user_service.domain.model.User;

@Repository
public interface UserRepository {
	
	public User save(User user) throws Exception;
	
	public boolean existsByUsernameAndEmail(String username, String email) throws SQLException;
	
}
