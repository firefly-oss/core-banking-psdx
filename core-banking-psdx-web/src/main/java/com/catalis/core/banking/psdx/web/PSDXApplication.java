package com.catalis.core.banking.psdx.web;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

/**
 * Main application class for the PSD2/PSD3 and FIDA Regulatory Compliance Service.
 * This service provides a centralized point for handling regulatory requirements
 * related to open banking and financial data access.
 *
 * Key features:
 * - Consent management for account information and payment initiation
 * - Strong Customer Authentication (SCA) implementation
 * - Third Party Provider (TPP) management
 * - Access logging and audit trail
 * - Integration with other core banking microservices
 */
@SpringBootApplication(
        scanBasePackages = {
                "com.catalis.core.banking.psdx",
                "com.catalis.common.web"  // Scan common web library configurations
        }
)
@EnableWebFlux
@EnableR2dbcRepositories(
        basePackages = "com.catalis.core.banking.psdx.models.repositories"
)
@EnableR2dbcAuditing
@ConfigurationPropertiesScan
@EnableAspectJAutoProxy
@OpenAPIDefinition(
        info = @Info(
                title = "${spring.application.name}",
                version = "${spring.application.version}",
                description = "${spring.application.description}",
                contact = @Contact(
                        name = "${spring.application.team.name}",
                        email = "${spring.application.team.email}"
                )
        ),
        servers = {
                @Server(
                        url = "http://core.catalis.vc/psdx",
                        description = "Development Environment"
                ),
                @Server(
                        url = "/",
                        description = "Local Development Environment"
                )
        }
)
public class PSDXApplication {

    /**
     * Main method to start the application.
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(PSDXApplication.class, args);
    }
}
