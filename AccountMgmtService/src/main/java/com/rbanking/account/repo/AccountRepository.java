package com.rbanking.account.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rbanking.account.entities.Account;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The AccountRepository interface provides methods to perform CRUD operations
 * on Account entities. It extends JpaRepository to leverage Spring Data JPA
 * functionalities.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	/**
	 * Finds an account by its account number.
	 *
	 * @param accountNumber the account number of the account to find
	 * @return an optional containing the account if found, empty otherwise
	 */
	Optional<Account> findByAccountNumber(Long accountNumber);

	/**
	 * Updates the minimum balance of accounts based on the age category of the
	 * customers.
	 *
	 * @param minBalance  the new minimum balance to be set
	 * @param ageCategory the age category of the customers whose accounts will be
	 *                    updated
	 */
	@Modifying
	@Transactional
	@Query(value = "UPDATE account AS ac SET min_balance = :minBalance FROM customer AS cu WHERE ac.cust_id = cu.cust_id AND cu.age_category = :ageCategory", nativeQuery = true)
	void updateMinBalanceByAgeCategory(Double minBalance, String ageCategory);

	/**
	 * Finds a list of accounts associated with a given customer ID.
	 *
	 * @param custId the ID of the customer whose accounts are to be retrieved
	 * @return an Optional containing a list of accounts for the specified customer
	 *         ID
	 */
	Optional<List<Account>> findByCustId(Long custId);

	@Query(value = "SELECT c.email FROM Account AS a JOIN Customer AS c ON a.cust_id = c.cust_id WHERE a.account_number = :accountNumber", nativeQuery = true)
	String findCustomerEmailByAccountNumber(@Param("accountNumber") Long accountNumber);
}
