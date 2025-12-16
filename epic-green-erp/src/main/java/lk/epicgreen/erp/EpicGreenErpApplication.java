package lk.epicgreen.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Epic Green ERP - Main Application Class
 * 
 * Spice Production Factory ERP System
 * Architecture: Monolithic
 * Framework: Spring Boot 3.2.5
 * Java Version: 21
 * 
 * @author Epic Green Development Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
@EnableScheduling
public class EpicGreenErpApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpicGreenErpApplication.class, args);
        System.out.println("""
            
            ╔═══════════════════════════════════════════════════════╗
            ║                                                       ║
            ║          Epic Green ERP System Started!              ║
            ║                                                       ║
            ║   Spice Production Factory Management System         ║
            ║   Version: 1.0.0                                     ║
            ║   Architecture: Monolithic                           ║
            ║                                                       ║
            ║   Swagger UI: http://localhost:8080/swagger-ui.html  ║
            ║   API Docs: http://localhost:8080/api-docs           ║
            ║   Actuator: http://localhost:8080/actuator           ║
            ║                                                       ║
            ╚═══════════════════════════════════════════════════════╝
            
            """);
    }
}
