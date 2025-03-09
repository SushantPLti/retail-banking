package com.rbanking.cservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.rbanking.cservice.customer.model.SuccessResponse;
import com.rbanking.cservice.customerDTO.ChangePasswordDTO;
import com.rbanking.cservice.customerDTO.CustomerDTO;
import com.rbanking.cservice.customerDTO.CustomersDisplayDTO;
import com.rbanking.cservice.customerDTO.UpdateDTO;
import com.rbanking.cservice.dto.LoginDTO;
import com.rbanking.cservice.entities.AgeCategory;
import com.rbanking.cservice.entities.Customer;
import com.rbanking.cservice.entities.Login;
import com.rbanking.cservice.entities.Role;
import com.rbanking.cservice.exception.CustomerNotFoundException;
import com.rbanking.cservice.exception.EmailException;
import com.rbanking.cservice.feing.NotificationClient;
import com.rbanking.cservice.repository.CustomerRepository;
import com.rbanking.cservice.repository.LoginRepository;
import com.rbanking.cservice.util.CustomerWrapper;
import com.rbanking.cservice.util.LoginWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomerServiceServicesTest {

	@Mock
	private CustomerRepository custRepo;

	@Mock
	private LoginRepository loginRepo;

	@Mock
	private LoginWrapper loginWrapper;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@InjectMocks
	private CustomerServiceServices customerService;

	@Mock
	private NotificationClient notificationClient;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetCustomerById_Success() {
		Long customerId = 1L;
		Customer customer = new Customer();
		customer.setCustId(customerId);
		when(custRepo.findById(customerId)).thenReturn(Optional.of(customer));

		CustomersDisplayDTO result = customerService.getCustomerbyId(customerId);

		assertEquals(customerId, result.getCustId());
	}

	@Test
	void testGetCustomerById_CustomerNotFound() {
		Long customerId = 1L;
		when(custRepo.findById(customerId)).thenReturn(Optional.empty());

		assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerbyId(customerId));
	}

	@Test
	void testCreateCustomer_Success() {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setPanNumber("ABCDE1234F");
		customerDTO.setAadhaarNumber(345687653412L);
		customerDTO.setPassword("password");
		customerDTO.setName("John Doe");
		customerDTO.setEmail("john.doe@example.com");
		customerDTO.setContactNo(1234567890L);
		customerDTO.setAddress("123 Main St");
		customerDTO.setDob(LocalDate.of(1990, 1, 1));

		Customer customer = CustomerWrapper.toCustomerEntity(customerDTO);
		customer.setCustId(1L);
		customer.setAgeCategory(AgeCategory.REGULAR);

		when(custRepo.findByPanNumber(customerDTO.getPanNumber())).thenReturn(Optional.empty());
		when(bCryptPasswordEncoder.encode(customerDTO.getPassword())).thenReturn("encodedPassword");
		when(custRepo.save(any(Customer.class))).thenReturn(customer);

		SuccessResponse response = customerService.createCustomer(customerDTO);

		assertEquals(HttpStatus.OK.value(), response.getResponseCode());
		assertNotNull(response.getData());
		Customer createdCustomer = (Customer) response.getData();
		assertEquals("ABCDE1234F", createdCustomer.getPanNumber());
		assertEquals(345687653412L, createdCustomer.getAadhaarNumber());
		assertEquals("John Doe", createdCustomer.getName());
		assertEquals("john.doe@example.com", createdCustomer.getEmail());
		assertEquals(1234567890L, createdCustomer.getContactNo());
		assertEquals("123 Main St", createdCustomer.getAddress());
		assertEquals(LocalDate.of(1990, 1, 1), createdCustomer.getDob());
		assertEquals(AgeCategory.REGULAR, createdCustomer.getAgeCategory());
	}

	@Test
	void testCreateCustomer_AlreadyExists() {
		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setPanNumber("ABCDE1234F");

		when(custRepo.findByPanNumber(customerDTO.getPanNumber())).thenReturn(Optional.of(new Customer()));

		assertThrows(EmailException.class, () -> customerService.createCustomer(customerDTO));
	}

	@Test
	void testValidateCustomer_Success() {
		Long custId = 123456L;
		Login login = new Login();
		login.setCustId(custId);

		when(loginRepo.findById(custId)).thenReturn(Optional.of(login));

		LoginDTO result = customerService.validateCustomer(custId);

		assertNotNull(result);
		assertEquals(custId, result.getCustId());
	}

	@Test
	void testValidateCustomer_CustomerNotFound() {
		Long custId = 123456L;

		when(loginRepo.findById(custId)).thenReturn(Optional.empty());

		assertThrows(CustomerNotFoundException.class, () -> {
			customerService.validateCustomer(custId);
		});
	}


	@Test
	void testGetAllCustomers() {
		List<Customer> customers = new ArrayList<>();
		customers.add(new Customer());
		when(custRepo.findAll()).thenReturn(customers);

		SuccessResponse response = customerService.getAllCustomers();

		assertEquals(HttpStatus.OK.value(), response.getResponseCode());
		assertNotNull(response.getData());
	}

	@Test
	public void testUpdateCustomerDetails_Success() {
		Long customerId = 1L;
		Long loggedInCustId = 2L;
		UpdateDTO customer = new UpdateDTO();
		customer.setAddress("New Address");
		customer.setContactNo(1234567890L);
		customer.setEmail("newemail@example.com");

		Customer existingCustomer = new Customer();
		existingCustomer.setCustId(customerId);
		existingCustomer.setName("Existing Customer");

		Customer loggedInCustomer = new Customer();
		loggedInCustomer.setCustId(loggedInCustId);
		loggedInCustomer.setName("Logged In Customer");

		Login existingLogin = new Login();
		existingLogin.setCustId(customerId);

		when(custRepo.findById(customerId)).thenReturn(Optional.of(existingCustomer));
		when(custRepo.findById(loggedInCustId)).thenReturn(Optional.of(loggedInCustomer));
		when(loginRepo.findById(customerId)).thenReturn(Optional.of(existingLogin));
		when(custRepo.save(any(Customer.class))).thenReturn(existingCustomer);
		when(loginRepo.save(any(Login.class))).thenReturn(existingLogin);

		SuccessResponse response = customerService.updateCustomerDetails(customerId, customer, loggedInCustId);

		assertNotNull(response);
		assertEquals(HttpStatus.OK.value(), response.getResponseCode());
		assertEquals("Customer account updated successfully", response.getMessage());
		assertEquals(existingCustomer, response.getData());
		assertEquals("Logged In Customer", existingCustomer.getUpdatedBy());
		assertEquals("newemail@example.com", existingCustomer.getEmail());
	}

	@Test
	public void testUpdateCustomerDetails_CustomerNotFound() {
		Long customerId = 1L;
		Long loggedInCustId = 2L;
		UpdateDTO customer = new UpdateDTO();

		when(custRepo.findById(customerId)).thenReturn(Optional.empty());

		assertThrows(CustomerNotFoundException.class, () -> {
			customerService.updateCustomerDetails(customerId, customer, loggedInCustId);
		});
	}

	@Test
	public void testUpdateCustomerDetails_LoginNotFound() {
		Long customerId = 1L;
		Long loggedInCustId = 2L;
		UpdateDTO customer = new UpdateDTO();

		Customer existingCustomer = new Customer();
		existingCustomer.setCustId(customerId);

		when(custRepo.findById(customerId)).thenReturn(Optional.of(existingCustomer));
		when(loginRepo.findById(customerId)).thenReturn(Optional.empty());

		assertThrows(CustomerNotFoundException.class, () -> {
			customerService.updateCustomerDetails(customerId, customer, loggedInCustId);
		});
	}

	@Test
	public void testUpdateCustomerDetails_LoggedInCustomerNotFound() {
		Long customerId = 1L;
		Long loggedInCustId = 2L;
		UpdateDTO customer = new UpdateDTO();

		Customer existingCustomer = new Customer();
		existingCustomer.setCustId(customerId);

		Login existingLogin = new Login();
		existingLogin.setCustId(customerId);

		when(custRepo.findById(customerId)).thenReturn(Optional.of(existingCustomer));
		when(loginRepo.findById(customerId)).thenReturn(Optional.of(existingLogin));
		when(custRepo.findById(loggedInCustId)).thenReturn(Optional.empty());

		assertThrows(CustomerNotFoundException.class, () -> {
			customerService.updateCustomerDetails(customerId, customer, loggedInCustId);
		});
	}

	@Test
	void testGetCustomersByRole_Success() {
		Role role = Role.CUSTOMER;
		Customer customer = new Customer();
		List<Customer> customers = Collections.singletonList(customer);

		when(custRepo.findCustomersByRole(role)).thenReturn(customers);

		SuccessResponse response = customerService.getCustomersByRole(role);

		assertEquals(HttpStatus.OK.value(), response.getResponseCode());
		assertEquals("Customers fetched successfully", response.getMessage());
		assertFalse(((List<CustomersDisplayDTO>) response.getData()).isEmpty());
	}

	@Test
	void testGetCustomersByRole_NoCustomersFound() {
		Role role = Role.CUSTOMER;

		when(custRepo.findCustomersByRole(role)).thenReturn(Collections.emptyList());

		SuccessResponse response = customerService.getCustomersByRole(role);

		assertNotNull(response);
		assertEquals(HttpStatus.OK.value(), response.getResponseCode());
		assertEquals("Customers fetched successfully", response.getMessage());
		assertTrue(((List<CustomersDisplayDTO>) response.getData()).isEmpty());
	}


	@Test
	public void testGetAgeCategoryByCustId_CustomerExists() {
		Customer customer = new Customer();
		customer.setCustId(1L);
		customer.setAgeCategory(AgeCategory.MINOR); // Example age category

		when(custRepo.findById(1L)).thenReturn(Optional.of(customer));

		String ageCategory = customerService.getAgeCategoryByCustId(1L);

		assertEquals("MINOR", ageCategory);
	}

	@Test
	public void testGetAgeCategoryByCustId_CustomerNotFound() {
		when(custRepo.findById(2L)).thenReturn(Optional.empty());

		Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
			customerService.getAgeCategoryByCustId(2L);
		});

		assertEquals("Customer not found with id: 2", exception.getMessage());
	}

	@Test
	void testLogout_Success() {
		Long custId = 123456L;

		// Mock the login repository to return a successful update count
		when(loginRepo.updateIsLoggedOutByCustId(custId, true)).thenReturn(1);

		SuccessResponse response = customerService.logout(custId);

		assertNotNull(response);
		assertEquals(HttpStatus.OK.value(), response.getResponseCode());
		assertEquals("You've successfully logged out!!", response.getMessage());
	}

	@Test
	void testLogout_CustomerNotFound() {
		Long custId = 123456L;

		// Mock the login repository to return zero, indicating no update
		when(loginRepo.updateIsLoggedOutByCustId(custId, true)).thenReturn(0);

		assertThrows(CustomerNotFoundException.class, () -> customerService.logout(custId));
	}

	@Test
	public void testChangeCustomerPassword_Success() {
		Long custId = 1L;
		String currentPassword = "currentPassword";
		String newPassword = "newPassword";
		ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
		changePasswordDTO.setCustId(custId);
		changePasswordDTO.setCurrentPassword(currentPassword);
		changePasswordDTO.setNewPassword(newPassword);

		Login login = new Login();
		login.setPassword("encryptedCurrentPassword");

		when(loginRepo.findById(custId)).thenReturn(Optional.of(login));
		when(bCryptPasswordEncoder.matches(currentPassword, login.getPassword())).thenReturn(true);
		when(bCryptPasswordEncoder.encode(newPassword)).thenReturn("encryptedNewPassword");

		SuccessResponse response = customerService.changeCustomerPassword(changePasswordDTO);

		assertEquals(200, response.getResponseCode());
		assertEquals("Password changed successfully for customer ID: " + custId, response.getMessage());
		assertEquals("Password change operation successful", response.getData());

		verify(loginRepo, times(1)).save(any(Login.class));
	}

	@Test
	public void testChangeCustomerPassword_CurrentPasswordIncorrect() {
		Long custId = 1L;
		String currentPassword = "currentPassword";
		String newPassword = "newPassword";
		ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
		changePasswordDTO.setCustId(custId);
		changePasswordDTO.setCurrentPassword(currentPassword);
		changePasswordDTO.setNewPassword(newPassword);

		Login login = new Login();
		login.setPassword("encryptedCurrentPassword");

		when(loginRepo.findById(custId)).thenReturn(Optional.of(login));
		when(bCryptPasswordEncoder.matches(currentPassword, login.getPassword())).thenReturn(false);

		assertThrows(IllegalArgumentException.class, () -> customerService.changeCustomerPassword(changePasswordDTO));
	}

	@Test
	public void testChangeCustomerPassword_CustomerNotFound() {
		Long custId = 1L;
		ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
		changePasswordDTO.setCustId(custId);
		changePasswordDTO.setCurrentPassword("currentPassword");
		changePasswordDTO.setNewPassword("newPassword");

		when(loginRepo.findById(custId)).thenReturn(Optional.empty());

		assertThrows(CustomerNotFoundException.class, () -> customerService.changeCustomerPassword(changePasswordDTO));
	}

}
