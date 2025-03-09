package com.rbanking.cservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.rbanking.cservice.entities.Customer;
import com.rbanking.cservice.entities.Role;


/**
 * Repository interface for handling customer-related database operations.
 */

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{

	/**
     * Finds a customer by their email.
     * @param email the email of the customer to find
     */
	Optional<Customer> findByEmail(String email);
	
//	Optional<Customer> findByEmailAndPassword(String email, String password);
	
	/**
     * Finds a customer by their contact number.
     * @param contactNo the contact number of the customer to find
     */
	Optional<Customer> findByContactNo(Long contactNo);

	/**
	 * Finds a customer by their PAN number.
	 *
	 * @param panNumber the PAN number of the customer
	 * @return an Optional containing the Customer if found, or an empty Optional if not found
	 */
	Optional<Customer> findByPanNumber(String panNumber);

	/**
	 * Finds customers by their role.
	 *
	 * @param role the role of the customers to be fetched
	 * @return a list of customers matching the specified role
	 */
	@Query("SELECT c FROM Customer c JOIN Login l ON c.custId = l.custId WHERE l.role = :role")
	List<Customer> findCustomersByRole(Role role);

}
