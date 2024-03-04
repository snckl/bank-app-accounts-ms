package com.cnsn.accounts;

import com.cnsn.accounts.dto.AccountsContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableConfigurationProperties(value = {AccountsContactInfoDto.class})
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableFeignClients // To enable feign client in accounts microservice.
@OpenAPIDefinition(
		info = @Info(
				title = "Accounts Microservices API Documentation",
				description = "Bank accounts microservices API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Sinan Cakal",
						email = "sinan.cakal@protonmail.com",
						url = "Not deployed yet"
				),
				license = @License(
						name = "Haven't licensed yet"	,
						url = "Not deployed yet"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Not external documentation"
		))
public class AccountsApplication {
	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}
}
