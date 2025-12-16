package lk.epicgreen.erp.common.audit;

import lk.epicgreen.erp.common.constants.AppConstants;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Provides the current auditor (user) for JPA auditing
 * Retrieves username from Spring Security context
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {
    
    /**
     * Gets the current auditor (logged-in user)
     * 
     * @return Current username or SYSTEM if no user authenticated
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.of(AppConstants.SYSTEM_USER);
        }
        
        // Anonymous user
        if ("anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.of(AppConstants.ANONYMOUS_USER);
        }
        
        // Get username
        String username = authentication.getName();
        
        return Optional.ofNullable(username != null ? username : AppConstants.SYSTEM_USER);
    }
}
