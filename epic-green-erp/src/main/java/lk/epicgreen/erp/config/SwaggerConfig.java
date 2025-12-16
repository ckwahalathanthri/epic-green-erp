package lk.epicgreen.erp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lk.epicgreen.erp.common.constants.AppConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI configuration
 * Configures API documentation using SpringDoc OpenAPI 3
 * 
 * Access Swagger UI at: http://localhost:8080/swagger-ui.html
 * Access API Docs at: http://localhost:8080/v3/api-docs
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Configuration
public class SwaggerConfig {
    
    @Value("${server.port:8080}")
    private String serverPort;
    
    @Value("${spring.application.name:Epic Green ERP}")
    private String applicationName;
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(apiInfo())
            .servers(serverList())
            .addSecurityItem(securityRequirement())
            .components(securityComponents());
    }
    
    /**
     * API information
     */
    private Info apiInfo() {
        return new Info()
            .title(AppConstants.APP_NAME + " API")
            .version(AppConstants.VERSION)
            .description(
                "RESTful API documentation for " + AppConstants.APP_NAME + " - " +
                "A comprehensive ERP system for spice production factory management.\n\n" +
                "**Features:**\n" +
                "- Supplier and Purchase Management\n" +
                "- Warehouse and Inventory Management\n" +
                "- Production Management\n" +
                "- Sales Order and Dispatch\n" +
                "- Customer and Payment Management\n" +
                "- Mobile Sales Operations\n" +
                "- Returns and Adjustments\n" +
                "- Accounting and Finance\n" +
                "- Reporting and Analytics\n\n" +
                "**Authentication:**\n" +
                "This API uses OAuth2 JWT Bearer token authentication. " +
                "Obtain a token from the /api/v1/auth/login endpoint and include it in the Authorization header."
            )
            .contact(apiContact())
            .license(apiLicense());
    }
    
    /**
     * API contact information
     */
    private Contact apiContact() {
        return new Contact()
            .name(AppConstants.COMPANY_NAME)
            .email("support@epicgreen.lk")
            .url("https://www.epicgreen.lk");
    }
    
    /**
     * API license information
     */
    private License apiLicense() {
        return new License()
            .name("Proprietary")
            .url("https://www.epicgreen.lk/license");
    }
    
    /**
     * Server list
     */
    private List<Server> serverList() {
        Server localServer = new Server()
            .url("http://localhost:" + serverPort)
            .description("Local Development Server");
        
        Server stagingServer = new Server()
            .url("https://staging-api.epicgreen.lk")
            .description("Staging Server");
        
        Server productionServer = new Server()
            .url("https://api.epicgreen.lk")
            .description("Production Server");
        
        return List.of(localServer, stagingServer, productionServer);
    }
    
    /**
     * Security requirement
     */
    private SecurityRequirement securityRequirement() {
        return new SecurityRequirement().addList("Bearer Authentication");
    }
    
    /**
     * Security components
     */
    private Components securityComponents() {
        return new Components()
            .addSecuritySchemes("Bearer Authentication", securityScheme());
    }
    
    /**
     * Security scheme for JWT Bearer token
     */
    private SecurityScheme securityScheme() {
        return new SecurityScheme()
            .name("Bearer Authentication")
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .description(
                "JWT Bearer token authentication. " +
                "Obtain token from /api/v1/auth/login endpoint. " +
                "Format: Bearer {token}"
            );
    }
}
