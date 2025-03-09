package com.rbanking.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AccountMgmtServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountMgmtServiceApplication.class, args);
	}

}
