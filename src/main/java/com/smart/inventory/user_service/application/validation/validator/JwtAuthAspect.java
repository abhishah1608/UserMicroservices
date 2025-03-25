package com.smart.inventory.user_service.application.validation.validator;

import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.smart.inventory.user_service.infrastructure.repository.JwtService;

@Aspect
@Component
public class JwtAuthAspect {

    
    private JwtService jwtService;

    private HttpServletRequest request;
    
    public JwtAuthAspect(JwtService jwtservice, HttpServletRequest request)
    {
        this.jwtService = jwtservice; 	
        this.request = request;
    }

    @Before("@annotation(com.smart.inventory.user_service.application.validation.annotation.JwtAuthRequired)")
    public void validateJwtToken(JoinPoint joinPoint) {
        String authHeader = request.getHeader("Authorization");
        String username= request.getHeader("username");
        String role = request.getHeader("role");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7); // Remove "Bearer "

        if (!jwtService.validateToken(token, username, role)) {
            throw new UnauthorizedException("Invalid JWT token");
        }
    }
}
