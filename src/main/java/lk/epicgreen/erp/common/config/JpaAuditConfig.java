package lk.epicgreen.erp.common.config;

import lk.epicgreen.erp.common.audit.AuditorAwareImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JpaAuditConfig
 * Configuration for JPA auditing
 * Enables automatic population of audit fields (createdBy, updatedBy, createdAt, updatedAt)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditConfig {
    
    /**
     * Bean for AuditorAware
     * Provides the current auditor (user ID) for JPA auditing
     * 
     * @return AuditorAware implementation
     */
    @Bean
    public AuditorAware<Long> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
