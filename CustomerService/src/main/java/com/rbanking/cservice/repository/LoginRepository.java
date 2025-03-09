package com.rbanking.cservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.rbanking.cservice.entities.Login;

public interface LoginRepository extends JpaRepository<Login, Long> {
	
    Optional<Login> findByEmail(String email);
  
    @Modifying
    @Transactional
    @Query(value = "UPDATE Login SET is_logged_out = :isLoggedOut WHERE cust_id = :custId", nativeQuery = true)
    int updateIsLoggedOutByCustId(@Param("custId") Long custId, @Param("isLoggedOut") boolean isLoggedOut);
}
