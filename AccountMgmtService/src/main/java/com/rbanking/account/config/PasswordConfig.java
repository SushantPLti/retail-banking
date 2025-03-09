package com.rbanking.account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean
    BCryptPasswordEncoder encode()
    {
        return new BCryptPasswordEncoder(10);
    }
}
