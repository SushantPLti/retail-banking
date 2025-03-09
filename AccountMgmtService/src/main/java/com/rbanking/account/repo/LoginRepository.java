package com.rbanking.account.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rbanking.account.entities.Login;
import com.rbanking.account.entities.Role;

public interface LoginRepository extends JpaRepository<Login, Long> {
	
    Optional<Login> findByEmail(String email);

    Role findRoleByEmail(String email);

}
