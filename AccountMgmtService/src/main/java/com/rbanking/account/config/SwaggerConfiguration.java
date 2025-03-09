package com.rbanking.account.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("Account Management service").description(
				"The account service microservice handles creating, updating, and deleting accounts, with JUnit testing for ensuring code reliability. It leverages Spring Boot for implementation, integrating secure and robust handling of account operations."))
				.addSecurityItem(new SecurityRequirement().addList("jwt security"))
				.components(new Components().addSecuritySchemes("jwt security",
						new SecurityScheme().name("Jwt security").type(SecurityScheme.Type.HTTP).scheme("bearer")));
	}
}