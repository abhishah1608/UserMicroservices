package com.smart.inventory.user_service.infrastructure.repository;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.smart.inventory.user_service.application.jwt.service.TokenProvider;
import com.smart.inventory.user_service.presentation.dto.UserDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService implements TokenProvider {

	
	private long expirationMillis = 3600000; 


	private String secretKey = "3cfa76ef14937c1c1EA819f4fc057A80fcd05a7420f8e84cd0a7567c212e005b"; 


	private long jwtExpiration = 3600000;

	public String extractUsername(String token) {
		return extractClaim(token, claims -> claims.get("username", String.class));
	}
	
	public String extractRole(String token) {
	    return extractClaim(token, claims -> claims.get("role", String.class));
	}


	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(UserDto userDetails) {
		
		HashMap<String,String> map = new HashMap<String, String>();
		map.put("username", userDetails.getUsername());
		map.put("role", userDetails.getRole());
		return generateToken(map, userDetails);
	}

	public String generateToken(Map<String, String> extraClaims, UserDto userDetails) {
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}

	public long getExpirationTime() {
		return jwtExpiration;
	}

	private String buildToken(Map<String, String> extraClaims, UserDto userDetails, long expiration) {
		String token = Jwts.builder().claims(extraClaims) // set custom claims
				.subject(userDetails.getUsername()).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + expirationMillis)).signWith(getSignInKey()).compact();
		return token;
	}

	public boolean validateToken(String token, String username, String role) {
		final String uname = extractUsername(token);
		final String rol = extractRole(token);
		return ( uname.equals(username) && rol.equals(role) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parser()
			    .verifyWith(getSignInKey())
			    .build()
			    .parseSignedClaims(token)
			    .getPayload();
	}

	
	private SecretKey getSignInKey() {
		byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes); // Automatically uses HS256
	}
	
}