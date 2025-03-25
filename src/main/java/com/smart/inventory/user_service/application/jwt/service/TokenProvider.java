package com.smart.inventory.user_service.application.jwt.service;

import com.smart.inventory.user_service.presentation.dto.UserDto;

public interface TokenProvider {
    String generateToken(UserDto userDetails);
    boolean validateToken(String token, String user, String role);
    String extractUsername(String token);
}
