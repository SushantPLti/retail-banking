package com.transaction.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.transaction.model.Login;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

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
    public String generateToken(Login login) {
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
    private Key getSignKey() {
        // Decode the base64 encoded secret key and return a Key object
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    /**
     * Extracts the userName from the JWT token.
     *
     * @param token the JWT token.
     * @return the userName contained in the token.
     */
    public String extractUserName(String token) {
        // Extract and return the subject claim from the token
        return extractClaim(token, Claims::getSubject);
    }


    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token.
     * @return the expiration date of the token.
     */
    public Date extractExpiration(String token) {
        // Extract and return the expiration claim from the token
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param token the JWT token.
     * @param claimResolver a function to extract the claim.
     * @param <T> the type of the claim.
     * @return the value of the specified claim.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        // Extract the specified claim using the provided function
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token the JWT token.
     * @return a Claims object containing all claims.
     */
    private Claims extractAllClaims(String token) {
        // Parse and return all claims from the token
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build().parseClaimsJws(token).getBody();
    }


    /**
     * Checks if the JWT token is expired.
     *
     * @param token the JWT token.
     * @return true if the token is expired, false otherwise.
     */
    public Boolean isTokenExpired(String token) {
        // Check if the token's expiration time is before the current time
        return extractExpiration(token).before(new Date());
    }

    /**
     * Validates the JWT token against the AuthModel.
     *
     * @param token the JWT token.
     * @param auth the AuthModel to validate against.
     * @return true if the token is valid, false otherwise.
     */
    public Boolean validateToken(String token, UserDetails auth) {
        // Extract username from token and check if it matches UserDetails' username
        final String userName = extractUserName(token);
        // Also check if the token is expired
        return (userName.equals(auth.getUsername()) && !isTokenExpired(token));
    }
}
