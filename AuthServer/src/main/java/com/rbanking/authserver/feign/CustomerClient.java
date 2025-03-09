package com.rbanking.authserver.feign;

import java.util.List;

import com.rbanking.authserver.dto.LoginDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.rbanking.authserver.model.AuthModel;

/**
 * Service interface for communicating with the Customer Service. This interface
 * uses Feign to create RESTful requests to the Customer Service.
 */
@Service
@FeignClient(name = "CustomerService")
public interface CustomerClient {

	/**
	 * Validates the customer's credentials by sending a POST request to the
	 * Customer Service's login endpoint.
	 *
	 * @param auth the authentication model containing the customer's login
	 *             credentials
	 * @return a list of customer roles if the customer is valid; otherwise, an
	 *         empty list
	 */
	@PostMapping("/customers/login")
	LoginDTO validateCustomer(@RequestBody AuthModel auth);

}
