package lk.epicgreen.erp.common.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * AuditorAwareImpl
 * Provides the current auditor (user ID) for JPA auditing
 * Automatically populates createdBy and updatedBy fields in AuditEntity
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
public class AuditorAwareImpl implements AuditorAware<Long> {
    
    /**
     * Get current auditor (logged-in user ID)
     * Returns user ID from Spring Security context
     * 
     * @return Optional containing user ID, or empty if not authenticated
     */
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        
        // If principal is anonymous or not authenticated
        if (authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }
        
        try {
            // If you're using custom UserDetails that has getId() method
            if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
                // You can cast to your custom UserDetails implementation
                // For now, we'll return a default value or parse from username
                // Example: return Optional.of(((CustomUserDetails) authentication.getPrincipal()).getId());
                
                // Temporary: If username is numeric, use it as ID
                String username = authentication.getName();
                try {
                    return Optional.of(Long.parseLong(username));
                } catch (NumberFormatException e) {
                    // If username is not numeric, return a system user ID (e.g., 1)
                    return Optional.of(1L);
                }
            }
            
            // If principal is directly a Long (user ID)
            if (authentication.getPrincipal() instanceof Long) {
                return Optional.of((Long) authentication.getPrincipal());
            }
            
            // If principal is a String that can be parsed as Long
            if (authentication.getPrincipal() instanceof String) {
                try {
                    return Optional.of(Long.parseLong((String) authentication.getPrincipal()));
                } catch (NumberFormatException e) {
                    return Optional.empty();
                }
            }
            
        } catch (Exception e) {
            // Log error if needed
            return Optional.empty();
        }
        
        return Optional.empty();
    }
}
