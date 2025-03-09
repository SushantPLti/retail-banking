package com.rbanking.account.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.rbanking.account.DTO.TransactionDto;

/**
 * Service interface for communicating with the Customer Service. This interface
 * uses Feign to create RESTful requests to the Customer Service.
 */
@Service
@FeignClient(name = "TRANSACTIONSERVICE")
public interface TransactionsClient {

    /**
     * Validates the customer's credentials by sending a POST request to the
     * Customer Service's login endpoint.
     *
     * @param custId the authentication model containing the customer's login
     *             credentials
     * @return a list of age category if the customer is valid; otherwise, an
     *         empty list
     */
    @PostMapping("/transaction")
    String updateBalance(@RequestBody TransactionDto dto);

}