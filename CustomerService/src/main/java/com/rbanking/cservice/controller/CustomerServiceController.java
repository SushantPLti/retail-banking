package com.rbanking.cservice.controller;

import com.rbanking.cservice.customerDTO.ChangePasswordDTO;
import com.rbanking.cservice.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rbanking.cservice.request.OtpRequest;
import com.rbanking.cservice.customer.model.AuthModel;
import com.rbanking.cservice.customer.model.SuccessResponse;
import com.rbanking.cservice.customerDTO.CustomerDTO;
import com.rbanking.cservice.customerDTO.CustomersDisplayDTO;
import com.rbanking.cservice.customerDTO.UpdateDTO;
import com.rbanking.cservice.dto.LoginDTO;
import com.rbanking.cservice.dto.LogoutDto;
import com.rbanking.cservice.entities.Role;
import com.rbanking.cservice.service.CustomerServiceServices;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Controller class for handling customer-related HTTP requests.
 */
@RestController
@RequestMapping("/customers")
@RefreshScope
public class CustomerServiceController {

	@Autowired
	private CustomerServiceServices custService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtService jwtService;

	/**
	 * Retrieves the account details of a customer by their ID.
	 *
	 * @param customerId the ID of the customer
	 */
	@Operation(summary = "Retrieve the account details of a customer by their ID")
	@GetMapping("customerById/{customerId}")
	@Cacheable(value = "customerAccountDetails", key = "#customerId")
	public ResponseEntity<CustomersDisplayDTO> customerAccountDetails(
			@PathVariable @Min(value = 1, message = "Customer ID must be greater than zero") Long customerId) {
		return new ResponseEntity<>(custService.getCustomerbyId(customerId), HttpStatus.OK);
	}

	/**
	 * Creates a new customer account.
	 *
	 * @param customer the customer to create
	 */
	@Operation(summary = "Create a new customer account")
	@PostMapping("/register")
	@CacheEvict(value = {"customerAccountDetails", "allCustomers"}, allEntries = true)
	public ResponseEntity<SuccessResponse> createAccount(@Valid @RequestBody CustomerDTO customer) {
		return new ResponseEntity<>(custService.createCustomer(customer), HttpStatus.CREATED);
	}

	/**
     * Validates a customer's credentials using their email and password.
     * @param auth the authentication model containing the customer's id and password
     */
	@Operation(summary = "Validate a customer's credentials using their customerID and password")
	@PostMapping("/login")
	public ResponseEntity<LoginDTO> validateCustomer(@RequestBody AuthModel auth){
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getCustID(), auth.getPassword()));

		if(authentication.isAuthenticated()){
			return new ResponseEntity<>(custService.validateCustomer(auth.getCustID()), HttpStatus.OK);
		} else
			throw new RuntimeException("unauthorized user");
	}

	@Operation(summary = "Logout user")
	@PostMapping("/logout")
	public ResponseEntity<SuccessResponse> logout(@RequestBody LogoutDto logoutDto) {
		return new ResponseEntity<>(custService.logout(logoutDto.getCustID()), HttpStatus.OK);
	}
	
	
	/**
	 * Retrieves a list of all customers.
	 */
	 @Operation(summary = "Retrieves a list of all customers")
	 @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	 @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER') or hasRole('ROLE_OPERATOR')")
	// public ResponseEntity<SuccessResponse> displayAllCustomers(){
	//   return new ResponseEntity<>(custService.getAllCustomers(), HttpStatus.OK);
	// }
	 @Cacheable(value = "allCustomers")
	 public Flux<CustomersDisplayDTO> displayAllCustomers(){
	   return custService.getAllCustomersFlux();
	 }

	@Operation(summary = "Update the details of a customer with the specified ID")
	@PutMapping("updateCustomer/{customerId}")
	@CachePut(value = "customerAccountDetails", key = "#customerId")
	@CacheEvict(value = "allCustomers", allEntries = true)
	public ResponseEntity<SuccessResponse> updateCustomerdetails(
			@PathVariable @Min(value = 1, message = "Customer ID must be greater than zero") long customerId,
			@RequestBody UpdateDTO customer,
			HttpServletRequest request) {

		// Extract the Bearer token from the Authorization header
		String authorizationHeader = request.getHeader("Authorization");
		String bearerToken = null;
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			bearerToken = authorizationHeader.substring(7);
		}

		long loggedInCustId = jwtService.extractUserId(bearerToken);

		return new ResponseEntity<>(custService.updateCustomerDetails(customerId, customer, loggedInCustId), HttpStatus.ACCEPTED);
	}


	@Operation(summary = "Retrieve customers by role")
	@GetMapping("customerByRole/{role}")
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_OPERATOR')")
	@Cacheable(value = "customersByRole", key = "#role")
	public ResponseEntity<SuccessResponse> displayCustomersByRole(
			@PathVariable Role role) {
		return new ResponseEntity<>(custService.getCustomersByRole(role), HttpStatus.OK);
	}

	@Operation(summary = "Retrieve the age category of a customer by their ID")
	@GetMapping("ageCategory/{customerId}")
	@Cacheable(value = "ageCategory", key = "#customerId")
	public ResponseEntity<String> getAgeCategory(@PathVariable
			@Min(value = 1, message = "Customer ID must be greater than zero") Long customerId) {
		return new ResponseEntity<>(custService.getAgeCategoryByCustId(customerId), HttpStatus.OK);
	}

	/**
	 * Endpoint to change the password of a customer.
	 *
	 * @param changePassword the ChangePasswordDTO containing the customer ID, current, and new passwords
	 * @return a ResponseEntity containing the SuccessResponse
	 */
	@PutMapping("/change-password")
	public ResponseEntity<SuccessResponse> changeCustomerPassword(@Valid @RequestBody ChangePasswordDTO changePassword) {
		SuccessResponse response = custService.changeCustomerPassword(changePassword);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/forget-password")
	public String sendForgetPassword(@RequestBody OtpRequest otpRequest) {
		String sentPassword = custService.sendPassword(otpRequest.getTo());
		return "Forget Password sent successfully through OTP on your EmailId! Your OTP is: " + sentPassword;
	}
}