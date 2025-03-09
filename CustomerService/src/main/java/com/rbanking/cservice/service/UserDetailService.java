package com.rbanking.cservice.service;

import com.rbanking.cservice.entities.Login;
import com.rbanking.cservice.exception.LoggedOutException;
import com.rbanking.cservice.repository.LoginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Primary
public class UserDetailService implements UserDetailsService {

	@Autowired
	LoginRepository loginRepository;
	
	Logger logger = LoggerFactory.getLogger(UserDetailService.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Login> user = loginRepository.findById(Long.parseLong(username));
			return org.springframework.security.core.userdetails.User.withUsername(user.get().getEmail())
					.password(user.get().getPassword())
					.roles(user.get().getRole().toString())
					.build();
	}

	public UserDetails loadUserByEmail(String username) throws UsernameNotFoundException {
		Optional<Login> user = loginRepository.findByEmail(username);
		if(user.isPresent() && !user.get().isLoggedOut()) {
			return org.springframework.security.core.userdetails.User.withUsername(user.get().getEmail())
					.password(user.get().getPassword())
					.roles(user.get().getRole().toString())
					.build();
		}else
			throw new LoggedOutException("Your session has expired. For security reasons, please log in again to continue.");
	}

}
