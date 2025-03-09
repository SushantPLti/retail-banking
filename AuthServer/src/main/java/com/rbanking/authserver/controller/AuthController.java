package com.rbanking.authserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import com.rbanking.authserver.model.AuthModel;
import com.rbanking.authserver.model.AuthResponse;
import com.rbanking.authserver.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController handles authentication-related requests. This controller
 * provides an endpoint for user login.
 * 
 */
@RestController
@RequestMapping("/auth")
@RefreshScope
public class AuthController {


	private AuthService authService;

	Logger logger = LoggerFactory.getLogger(AuthController.class);

	/**
	 * Constructor for AuthController
	 * 
	 * @param authService the authentication service to be used
	 */
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	/**
	 * Handles login requests.
	 * 
	 * @param auth the authentication model containing login credentials
	 * @return ResponseEntity containing the authentication response and HTTP status
	 */
	@Operation(summary = "Validate a customer's credentials using their id and password")
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody AuthModel auth) throws Exception {
			return new ResponseEntity<>(authService.generateToken(auth), HttpStatus.OK);
	}

}
