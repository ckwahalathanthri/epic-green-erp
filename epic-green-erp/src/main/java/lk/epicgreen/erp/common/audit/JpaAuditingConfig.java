package lk.epicgreen.erp.common.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration for JPA auditing
 * Enables automatic population of audit fields (@CreatedBy, @CreatedDate, etc.)
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {
    
    /**
     * Provides the auditor provider bean
     * 
     * @return AuditorAware implementation
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }
}
