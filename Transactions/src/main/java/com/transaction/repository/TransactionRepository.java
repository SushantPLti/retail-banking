package com.transaction.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.transaction.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	@Query(value = "select * from transaction where account_number = :accountNumber order by transaction_id desc;", nativeQuery = true)
	Page<Transaction> findAllTransactionsByAccountNumber(Long accountNumber, Pageable pageable);

	List<Transaction> findByAccountNumberOrderByTransactionIdDesc(Long accountNumber);

	boolean existsByAccountNumber(Long referenceNumber);

}
