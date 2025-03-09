package com.rbanking.cservice.config;

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
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("Customer service").description(
				"The customer service microservice handles creating, viewing (admin-only), and updating customer records with JWT security and PostgreSQL for data storage using Spring Boot. It includes features like custom error handling, logging, and API documentation."))
				.addSecurityItem(new SecurityRequirement().addList("jwt security"))
				.components(new Components().addSecuritySchemes("jwt security",
						new SecurityScheme().name("Jwt security").type(SecurityScheme.Type.HTTP).scheme("bearer")));
	}
}