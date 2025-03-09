package com.rbanking.cservice.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.rbanking.cservice.customerDTO.ChangePasswordDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rbanking.cservice.customer.model.SuccessResponse;
import com.rbanking.cservice.customerDTO.CustomerDTO;
import com.rbanking.cservice.customerDTO.CustomersDisplayDTO;
import com.rbanking.cservice.customerDTO.UpdateDTO;
import com.rbanking.cservice.dto.LoginDTO;
import com.rbanking.cservice.entities.AgeCategory;
import com.rbanking.cservice.entities.Customer;
import com.rbanking.cservice.entities.CustomerStatus;
import com.rbanking.cservice.entities.Login;
import com.rbanking.cservice.entities.Role;
import com.rbanking.cservice.exception.CustomerInactiveException;
import com.rbanking.cservice.exception.CustomerNotFoundException;
import com.rbanking.cservice.exception.EmailException;
import com.rbanking.cservice.feing.NotificationClient;
import com.rbanking.cservice.repository.CustomerRepository;
import com.rbanking.cservice.repository.LoginRepository;
import com.rbanking.cservice.request.OtpRequest;
import com.rbanking.cservice.util.CustomerWrapper;
import com.rbanking.cservice.util.LoginWrapper;

import reactor.core.publisher.Flux;

/**
 * Service class for handling customer-related operations.
 */
@Service
public class CustomerServiceServices {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceServices.class);

	@Autowired
	private CustomerRepository custRepo;

	@Autowired
	private LoginRepository loginRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private NotificationClient notificationClient;

	@Value("${customer.age.minor}")
	int minorAge;

	@Value("${customer.age.senior}")
	int seniorAge;

	/**
	 * Retrieves a customer by their ID.
	 * 
	 * @param customerId the ID of the customer to retrieve the details
	 */

	public CustomersDisplayDTO getCustomerbyId(Long customerId) {
		Optional<Customer> optionalCustomer = custRepo.findById(customerId);
		if (optionalCustomer.isPresent()) {
			Customer customer = optionalCustomer.get();
			CustomersDisplayDTO customerDetails = CustomerWrapper.toCustomerDetails(customer);

			logger.info("getCustomerbyId- "+ customerId+ ", "+customer.getName());
			return customerDetails;
		} else {
			throw new CustomerNotFoundException("No Customer Found with this CustomerID : " + customerId);
		}

	}

	/**
	 * Creates a new customer.
	 *
	 * @param customer the customer to create
	 */
	@Transactional
	public SuccessResponse createCustomer(CustomerDTO customer) {
		if (custRepo.findByPanNumber(customer.getPanNumber()).isPresent()) {
			throw new EmailException("User already exists with this PanNumber : " + customer.getPanNumber());
		}
		Customer dbCustomer = CustomerWrapper.toCustomerEntity(customer);

		int age = calculateAge(customer.getDob(), LocalDate.now());
		AgeCategory ageCategory = getAgeCategory(age);
		dbCustomer.setAgeCategory(ageCategory);

		Customer cust = custRepo.save(dbCustomer);

		String encryptedPassword = passwordEncoder.encode(customer.getPassword());
		Login loginInfo = new Login(cust.getCustId(), customer.getEmail(), encryptedPassword, customer.getRole());
		loginRepository.save(loginInfo);

		SuccessResponse response = new SuccessResponse();
		response.setResponseCode(HttpStatus.OK.value());
		response.setMessage("Customer created successfully! Your customer ID is: "+cust.getCustId());
		response.setData(cust);
		return response;
	}

	private int calculateAge(LocalDate birthDate, LocalDate currentDate) {
		if ((birthDate != null) && (currentDate != null)) {
			return Period.between(birthDate, currentDate).getYears();
		} else {
			return 0;
		}
	}

	private AgeCategory getAgeCategory(int age) {
		if (age < minorAge) {
			return AgeCategory.MINOR;
		} else if (age < seniorAge) {
			return AgeCategory.REGULAR;
		} else {
			return AgeCategory.SENIOR_CITIZEN;
		}
	}

	/**
	 * Validates a customer's credentials.
	 * 
//	 * @param email the authentication model containing the customer's email and
	 *             password
	 */
	public LoginDTO validateCustomer(Long custId) {

		Optional<Login> optLogin = loginRepository.findById(custId);
		if (optLogin.isPresent()) {
			Optional<Customer> optionalCustomer = custRepo.findById(custId);
			if(optionalCustomer.isPresent() && optionalCustomer.get().getCustomerStatus().equals(CustomerStatus.INACTIVE)){
				throw new CustomerInactiveException("Your account is currently inactive. Please contact customer support for assistance.");
			} else {
				loginRepository.updateIsLoggedOutByCustId(custId, false);
				return LoginWrapper.toLoginDto(optLogin.get());
			}
		} else
			throw new CustomerNotFoundException("No Customer Found with this customer Id " + custId);

	}

	/**
	 * Retrieves a list of all customers.
	 */
	public SuccessResponse getAllCustomers() {
		List<Customer> customers = custRepo.findAll();
		List<CustomersDisplayDTO> customerDTOs = new ArrayList<>();
		for (Customer customer : customers) {
			CustomersDisplayDTO customersdisplayDTO = CustomerWrapper.toCustomerDetails(customer);
			customerDTOs.add(customersdisplayDTO);
		}
		SuccessResponse response = new SuccessResponse();
		response.setResponseCode(HttpStatus.OK.value());
		response.setMessage("Customer list retrieved successfully");
		response.setData(customerDTOs);
		return response;
	}

	/**
	 * Updates the details of a customer with the specified ID.
	 * 
	 * @param customerId the ID of the customer to update
	 * @param customer   the updated customer details
	 */
	public SuccessResponse updateCustomerDetails(Long customerId, UpdateDTO customer, Long loggedInCustId) {
		Optional<Customer> optionalCustomer = custRepo.findById(customerId);
		Optional<Login> optionalLogin = loginRepository.findById(customerId);
		Optional<Customer> optionalLoggedInCustomer = custRepo.findById(loggedInCustId);

		if (optionalCustomer.isPresent() && optionalLogin.isPresent() && optionalLoggedInCustomer.isPresent()) {
			Customer cust = optionalCustomer.get();
			Customer loggedInCustomer = optionalLoggedInCustomer.get();

			// Update only allowed fields
			if (customer.getAddress() != null) {
				cust.setAddress(customer.getAddress());
			}
			if (customer.getContactNo() != null) {
				cust.setContactNo(customer.getContactNo());
			}
			if (customer.getEmail() != null) {
				cust.setEmail(customer.getEmail());
			}
			if (customer.getUpdatedBy() == null && loggedInCustomer.getName() != null) {
				cust.setUpdatedBy(loggedInCustomer.getName());
			}
			if (customer.getName() != null) {
				cust.setName(customer.getName());
			}
			if (customer.getAgeCategory() != null) {
				cust.setAgeCategory(customer.getAgeCategory());
			}
			if (customer.getCustomerStatus() != null) {
				cust.setCustomerStatus(customer.getCustomerStatus());
			}

			Customer updatedCustomer = custRepo.save(cust);
			SuccessResponse response = new SuccessResponse();
			response.setResponseCode(HttpStatus.OK.value());
			response.setMessage("Customer account updated successfully");
			response.setData(updatedCustomer);

			// Update email in login table
			Login login = optionalLogin.get();
			login.setEmail(customer.getEmail());

			loginRepository.save(login);

			return response;
		} else {
			throw new CustomerNotFoundException("No Customer Found with this ID: " + customerId);
		}
	}

	/**
	 * Fetches customers by their role and returns a SuccessResponse containing customer details.
	 * @param role the role of the customers to be fetched
	 * @return a SuccessResponse containing a list of CustomersDisplayDTO and an HTTP status code of OK
	 */
	public SuccessResponse getCustomersByRole(Role role) {

		List<Customer> customers = custRepo.findCustomersByRole(role);
		List<CustomersDisplayDTO> customerDTOs = customers .stream() .map(CustomerWrapper::toCustomerDetails) .collect(Collectors.toList());
		SuccessResponse response = new SuccessResponse();
		response.setResponseCode(HttpStatus.OK.value());
		response.setMessage("Customers fetched successfully");
		response.setData(customerDTOs);
		return response;
	}

	/**
	 * Fetches the age category of a customer by their ID.
	 * @param customerId the ID of the customer whose age category is to be fetched
	 * @return the age category of the customer as a String
	 * @throws CustomerNotFoundException if no customer is found with the provided ID
	 */
	public String getAgeCategoryByCustId(Long customerId) {
		Optional<Customer> customerOpt = custRepo.findById(customerId);
		if (customerOpt.isPresent()) {
			Customer customer = customerOpt.get();
			return customer.getAgeCategory().toString();
		} else {
			throw new CustomerNotFoundException("Customer not found with id: " + customerId);
		}
	}

    public SuccessResponse logout(Long custId) {
		int loggedOut = loginRepository.updateIsLoggedOutByCustId(custId, true);
		if(loggedOut > 0) {
			SuccessResponse response = new SuccessResponse();
			response.setResponseCode(HttpStatus.OK.value());
			response.setMessage("You've successfully logged out!!");
			return response;
		}
		else
			throw new CustomerNotFoundException("Customer not found with customer Id: " + custId);
    }


    public Flux<CustomersDisplayDTO> getAllCustomersFlux() {
     List<Customer> customers = custRepo.findAll();
     List<CustomersDisplayDTO> customerDTOs = new ArrayList<>();
     for (Customer customer : customers) {
      CustomersDisplayDTO customersdisplayDTO = CustomerWrapper.toCustomerDetails(customer);
      customerDTOs.add(customersdisplayDTO);
     }
     return Flux.fromIterable(customerDTOs).delayElements(Duration.ofMillis(1000));
    }

	/**
	 * Changes the password for a customer with the specified ID in the request body.
	 *
	 * @param changePasswordDTO the ChangePasswordDTO containing the customer ID, current, and new passwords
	 * @return a SuccessResponse indicating the result of the operation
	 */
	public SuccessResponse changeCustomerPassword(@Valid ChangePasswordDTO changePasswordDTO) {
		Long custId = changePasswordDTO.getCustId();
		Optional<Login> optLogin = loginRepository.findById(custId);
		if (optLogin.isPresent()) {
			Login login = optLogin.get();
			if (passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), login.getPassword())) {
				String encryptedPassword = passwordEncoder.encode(changePasswordDTO.getNewPassword());
				login.setPassword(encryptedPassword);
				loginRepository.save(login);

				SuccessResponse response = new SuccessResponse();
				response.setResponseCode(HttpStatus.OK.value());
				response.setMessage("Password changed successfully for customer ID: " + custId);
				response.setData("Password change operation successful");
				return response;
			} else {
				throw new IllegalArgumentException("Username or Password is incorrect");
			}
		} else {
			throw new CustomerNotFoundException("No Customer Found with this customer Id: " + custId);
		}
	}


    public String sendPassword(String to) {
    	OtpRequest otpRequest = new OtpRequest();
        otpRequest.setTo(to);
        return notificationClient.sendOtp(otpRequest);
    }

}