package com.rbanking.account.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.rbanking.account.entities.Login;
import com.rbanking.account.entities.Role;
import com.rbanking.account.repo.LoginRepository;


@ExtendWith(MockitoExtension.class)
class UserDetailServiceTest {

    @InjectMocks
    private UserDetailService userDetailService;

    @Mock
    private LoginRepository loginRepository;

    private Login sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new Login();
        sampleUser.setEmail("test@example.com");
        sampleUser.setPassword("password");
        sampleUser.setRole(Role.CUSTOMER);
    }

    @Test
    void testLoadUserByUsername() {
        when(loginRepository.findByEmail("test@example.com")).thenReturn(Optional.of(sampleUser));
        
        UserDetails userDetails = userDetailService.loadUserByUsername("test@example.com");

        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
    }

    @Test
    void testConvertRolesToAuthorities() {
        List<Role> roles = List.of(Role.CUSTOMER, Role.CUSTOMER);

        List<SimpleGrantedAuthority> authorities = UserDetailService.convertRolesToAuthorities(roles);

        assertNotNull(authorities);
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("CUSTOMER")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("CUSTOMER")));
    }

}
