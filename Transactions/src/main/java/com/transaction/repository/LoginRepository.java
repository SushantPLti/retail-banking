package com.transaction.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.transaction.model.Login;
import com.transaction.model.Role;

public interface LoginRepository extends JpaRepository<Login, Long> {
	
    Optional<Login> findByEmail(String email);

    Role findRoleByEmail(String email);

}
