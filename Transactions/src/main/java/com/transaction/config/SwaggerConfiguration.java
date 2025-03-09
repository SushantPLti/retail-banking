package com.transaction.config;

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
		return new OpenAPI().info(new Info().title("Transaction service").description(
				"The transactions service microservice handles making credit and debit transactions, viewing account statements, and fund transfers. It leverages Spring Boot for implementation, ensuring secure and efficient handling of financial operations."))
				.addSecurityItem(new SecurityRequirement().addList("jwt security"))
				.components(new Components().addSecuritySchemes("jwt security",
						new SecurityScheme().name("Jwt security").type(SecurityScheme.Type.HTTP).scheme("bearer")));
	}
}