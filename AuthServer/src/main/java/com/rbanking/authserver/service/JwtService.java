package com.rbanking.authserver.service;

import com.rbanking.authserver.dto.LoginDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for handling JSON Web Tokens (JWT) related operations.
 */
@Component
public class JwtService {

	@Value("${jwt.secret}")
	String secret;
	
	Logger logger = LoggerFactory.getLogger(JwtService.class);
	

    /**
     * Generates a JWT token for the given userName.
     *
     * @param userName the userName for whom the token is to be generated.
     * @param roles the roles associated with the user.
     * @return a JWT token as a string.
     */
    public String generateToken(LoginDTO login) {
		logger.info("token generateToken");

        // Prepare claims for the token
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", List.of(login.getRole().toString()));
        claims.put("custId", login.getCustId());

        // Build JWT token with claims, subject, issued time, expiration time, and signing algorithm
          // Token valid for 3 minutes
        String jwt= Jwts.builder()
                .setClaims(claims)
                .setSubject(login.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))  //30 min
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
        
		logger.info("token "+jwt);

        return jwt;
    }


    /**
     * Creates a signing key from the base64 encoded secret.
     *
     * @return a Key object for signing the JWT.
     */
    public Key getSignKey() {
        // Decode the base64 encoded secret key and return a Key object
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
