package com.rbanking.authserver.service;

import com.rbanking.authserver.dto.LoginDTO;
import com.rbanking.authserver.exception.CustomerInactiveException;
import com.rbanking.authserver.exception.UnauthorizedException;
import com.rbanking.authserver.feign.CustomerClient;
import com.rbanking.authserver.model.AuthModel;
import com.rbanking.authserver.model.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The AuthService class provides methods for validating user authentication.
 * It interacts with the CustomerClient to validate customer roles and generates
 * JWT tokens using JwtService.
 * 
 * <p>This service class is annotated with @Service, making it a candidate for
 * Spring's component scanning to detect and register as a bean.</p>
 * 
 * <p>Dependencies are injected using @Autowired annotation.</p>
 * 
 * <p>Logging is performed using SLF4J Logger.</p>
 * 
 * @author Your Name
 */
@Service
public class AuthService {
	
	@Autowired
	private CustomerClient customerClient;

	@Autowired
	private JwtService jwtService;

	private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
	
	private static final String BEARER = "Bearer";


    /**
	 * Validates the authentication details of a user.
	 *
	 * @param auth        the authentication details of the user
	 * @return an AuthResponse containing the access token and token type
	 * @throws UnauthorizedException if the user is unauthorized
	 */
	public AuthResponse generateToken(AuthModel auth) throws Exception {

		try {
		LoginDTO loginDTO = customerClient.validateCustomer(auth);
			if (loginDTO != null) {
				String token = jwtService.generateToken(loginDTO);
				AuthResponse authResponse = new AuthResponse();
				authResponse.setAccessToken(token);
				authResponse.setTokenType(BEARER);
				return authResponse;
			} else {
				throw new UnauthorizedException("No Customer Found with this custId "+auth.getCustID());
			}
		} catch (Exception exception) {
			if(exception.getMessage().contains("inactive"))
				throw new CustomerInactiveException("Your account is inactive, please connect with ADMIN");
			else if(exception.getMessage().contains("Bad credentials"))
				throw new UnauthorizedException("Incorrect customer Id or password. Please try again.");
			else if(exception.getMessage().contains("No value present"))
				throw new UnauthorizedException("Incorrect customer Id or password. Please try again.");
			else
				throw new Exception(exception.getMessage());
		}
	}

}
