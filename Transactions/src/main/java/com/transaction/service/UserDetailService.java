package com.transaction.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.ws.rs.ForbiddenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.transaction.model.Login;
import com.transaction.model.Role;
import com.transaction.repository.LoginRepository;

@Service
@Primary
public class UserDetailService implements UserDetailsService {

	@Autowired
	LoginRepository loginRepository;
	
	Logger logger = LoggerFactory.getLogger(UserDetailService.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Login> user = loginRepository.findByEmail(username);
		if(user.isPresent() && !user.get().isLoggedOut()) {
			return org.springframework.security.core.userdetails.User.withUsername(user.get().getEmail())
					.password(user.get().getPassword())
					.roles(user.get().getRole().toString())
					.build();
		}else
			throw new ForbiddenException("Your session has expired. For security reasons, please log in again to continue.");
	}

	public static List<SimpleGrantedAuthority> convertRolesToAuthorities(List<Role> roles) {
		return roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.toString()))
				.collect(Collectors.toList());
	}

}
