package com.rbanking.cservice.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.rbanking.cservice.customer.model.AuthModel;
import com.rbanking.cservice.customer.model.SuccessResponse;
import com.rbanking.cservice.customerDTO.ChangePasswordDTO;
import com.rbanking.cservice.customerDTO.CustomerDTO;
import com.rbanking.cservice.customerDTO.CustomersDisplayDTO;
import com.rbanking.cservice.customerDTO.UpdateDTO;
import com.rbanking.cservice.dto.LogoutDto;
import com.rbanking.cservice.dto.LoginDTO;
import com.rbanking.cservice.entities.Role;
import com.rbanking.cservice.exception.CustomerNotFoundException;
import com.rbanking.cservice.service.CustomerServiceServices;

import com.rbanking.cservice.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceControllerTest {

    @Mock
    private CustomerServiceServices custService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private CustomerServiceController controller;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCustomerAccountDetails_Success() {
        Long customerId = 1L;
        CustomersDisplayDTO customerDTO = new CustomersDisplayDTO();
        customerDTO.setCustId(customerId);

        when(custService.getCustomerbyId(customerId)).thenReturn(customerDTO);

        ResponseEntity<CustomersDisplayDTO> response = controller.customerAccountDetails(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerId, response.getBody().getCustId());
    }

    @Test
    void testCreateAccount_Success() {
        CustomerDTO customerDTO = new CustomerDTO();
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setResponseCode(HttpStatus.CREATED.value());

        when(custService.createCustomer(any(CustomerDTO.class))).thenReturn(successResponse);

        ResponseEntity<SuccessResponse> response = controller.createAccount(customerDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(HttpStatus.CREATED.value(), response.getBody().getResponseCode());
    }

    @Test
    void testValidateCustomer_Success() {
        AuthModel authModel = new AuthModel();
        authModel.setCustID(123456L);
        authModel.setPassword("password");

        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(custService.validateCustomer(authModel.getCustID())).thenReturn(loginDTO);

        ResponseEntity<LoginDTO> response = controller.validateCustomer(authModel);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test@example.com", response.getBody().getEmail());
    }



    @Test
    public void testUpdateCustomerdetails_Success() {
        Long customerId = 1L;
        Long loggedInCustId = 2L;
        UpdateDTO customer = new UpdateDTO();
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setResponseCode(HttpStatus.OK.value());
        successResponse.setMessage("Customer account updated successfully");

        when(request.getHeader("Authorization")).thenReturn("Bearer some-valid-jwt-token");
        when(jwtService.extractUserId(anyString())).thenReturn(loggedInCustId);
        when(custService.updateCustomerDetails(anyLong(), any(UpdateDTO.class), anyLong())).thenReturn(successResponse);

        ResponseEntity<SuccessResponse> responseEntity = controller.updateCustomerdetails(customerId, customer, request);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals(successResponse, responseEntity.getBody());
    }

    @Test
    public void testUpdateCustomerdetails_CustomerNotFound() {
        Long customerId = 1L;
        Long loggedInCustId = 2L;
        UpdateDTO customer = new UpdateDTO();

        when(request.getHeader("Authorization")).thenReturn("Bearer some-valid-jwt-token");
        when(jwtService.extractUserId(anyString())).thenReturn(loggedInCustId);
        doThrow(new CustomerNotFoundException("No Customer Found with this ID: " + customerId))
                .when(custService).updateCustomerDetails(anyLong(), any(UpdateDTO.class), anyLong());

        try {
            controller.updateCustomerdetails(customerId, customer, request);
        } catch (CustomerNotFoundException e) {
            assertEquals("No Customer Found with this ID: " + customerId, e.getMessage());
        }
    }

    @Test
    void testDisplayAllCustomers_Success() {
        // Creating dummy data
        CustomersDisplayDTO customer1 = new CustomersDisplayDTO();
        customer1.setCustId(1L);
        customer1.setName("John Doe");

        CustomersDisplayDTO customer2 = new CustomersDisplayDTO();
        customer2.setCustId(2L);
        customer2.setName("Jane Doe");

        List<CustomersDisplayDTO> customers = Arrays.asList(customer1, customer2);

        // Mocking the service method
        when(custService.getAllCustomersFlux()).thenReturn(Flux.fromIterable(customers));

        // Calling the controller method
        Flux<CustomersDisplayDTO> responseFlux = controller.displayAllCustomers();

        // Collecting the results to a list
        List<CustomersDisplayDTO> resultList = responseFlux.collectList().block();

        // Asserting the results
        assertEquals(2, resultList.size());
        assertEquals("John Doe", resultList.get(0).getName());
        assertEquals("Jane Doe", resultList.get(1).getName());
    }

    @Test
    void testDisplayCustomersByRole_Success() {
        Role role = Role.CUSTOMER;
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setResponseCode(HttpStatus.OK.value());

        when(custService.getCustomersByRole(role)).thenReturn(successResponse);

        ResponseEntity<SuccessResponse> response = controller.displayCustomersByRole(role);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.OK.value(), response.getBody().getResponseCode());
    }

    @Test
    void testGetAgeCategory_Success() {
        Long customerId = 1L;
        String ageCategory = "ADULT";

        when(custService.getAgeCategoryByCustId(customerId)).thenReturn(ageCategory);

        ResponseEntity<String> response = controller.getAgeCategory(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ageCategory, response.getBody());
    }

    @Test
    void testLogout_Success() {
        LogoutDto logoutDto = new LogoutDto();
        logoutDto.setCustID(123456L);

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setResponseCode(HttpStatus.OK.value());

        when(custService.logout(anyLong())).thenReturn(successResponse);

        ResponseEntity<SuccessResponse> response = controller.logout(logoutDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.OK.value(), response.getBody().getResponseCode());
    }

    @Test
    void testChangeCustomerPassword_Success() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setCustId(1L);
        changePasswordDTO.setCurrentPassword("currentPassword");
        changePasswordDTO.setNewPassword("newPassword");

        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setResponseCode(200);
        successResponse.setMessage("Password changed successfully for customer ID: 1");
        successResponse.setData("Password change operation successful");

        when(custService.changeCustomerPassword(any(ChangePasswordDTO.class))).thenReturn(successResponse);

        ResponseEntity<SuccessResponse> response = controller.changeCustomerPassword(changePasswordDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successResponse, response.getBody());
    }

}
