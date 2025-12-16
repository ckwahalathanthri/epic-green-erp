package lk.epicgreen.erp.config;

import lk.epicgreen.erp.common.constants.ApiEndpoints;
import lk.epicgreen.erp.common.constants.RolesAndPermissions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Security configuration for Epic Green ERP
 * Configures OAuth2 JWT authentication and authorization
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtTokenFilter jwtTokenFilter;
    
    /**
     * Configure security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for stateless JWT authentication
            .csrf(AbstractHttpConfigurer::disable)
            
            // Configure CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Configure exception handling
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
            )
            
            // Set session management to stateless
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Configure authorization
            .authorizeHttpRequests(auth -> auth
                // Public endpoints - no authentication required
                .requestMatchers(
                    ApiEndpoints.AUTH_BASE + "/**",
                    "/api/v1/public/**",
                    "/actuator/health",
                    "/actuator/info"
                ).permitAll()
                
                // Swagger/OpenAPI endpoints
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-resources/**",
                    "/webjars/**"
                ).permitAll()
                
                // Static resources
                .requestMatchers(
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/favicon.ico"
                ).permitAll()
                
                // Error endpoint
                .requestMatchers("/error").permitAll()
                
                // Admin endpoints - require ADMIN role
                .requestMatchers("/api/v1/admin/**")
                    .hasRole("ADMIN")
                
                // User management endpoints
                .requestMatchers(HttpMethod.GET, ApiEndpoints.USERS_BASE + "/**")
                    .hasAuthority(RolesAndPermissions.PERM_VIEW_USERS)
                .requestMatchers(HttpMethod.POST, ApiEndpoints.USERS_BASE)
                    .hasAuthority(RolesAndPermissions.PERM_CREATE_USERS)
                .requestMatchers(HttpMethod.PUT, ApiEndpoints.USERS_BASE + "/**")
                    .hasAuthority(RolesAndPermissions.PERM_EDIT_USERS)
                .requestMatchers(HttpMethod.DELETE, ApiEndpoints.USERS_BASE + "/**")
                    .hasAuthority(RolesAndPermissions.PERM_DELETE_USERS)
                
                // Product endpoints
                .requestMatchers(HttpMethod.GET, ApiEndpoints.PRODUCTS_BASE + "/**")
                    .hasAuthority(RolesAndPermissions.PERM_VIEW_PRODUCTS)
                .requestMatchers(HttpMethod.POST, ApiEndpoints.PRODUCTS_BASE)
                    .hasAuthority(RolesAndPermissions.PERM_CREATE_PRODUCTS)
                .requestMatchers(HttpMethod.PUT, ApiEndpoints.PRODUCTS_BASE + "/**")
                    .hasAuthority(RolesAndPermissions.PERM_EDIT_PRODUCTS)
                .requestMatchers(HttpMethod.DELETE, ApiEndpoints.PRODUCTS_BASE + "/**")
                    .hasAuthority(RolesAndPermissions.PERM_DELETE_PRODUCTS)
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            
            // Add JWT token filter
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    /**
     * Configure CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allowed origins
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200",      // Angular dev server
            "http://localhost:3000",      // React dev server (if needed)
            "https://epicgreen.lk",       // Production domain
            "https://www.epicgreen.lk"    // Production www domain
        ));
        
        // Allowed methods
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));
        
        // Allowed headers
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Accept",
            "X-Requested-With",
            "X-XSRF-TOKEN"
        ));
        
        // Exposed headers
        configuration.setExposedHeaders(Arrays.asList(
            "Authorization",
            "X-Total-Count",
            "X-Page-Number",
            "X-Page-Size"
        ));
        
        // Allow credentials
        configuration.setAllowCredentials(true);
        
        // Max age
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
    
    /**
     * JWT authentication converter
     * Converts JWT claims to Spring Security authorities
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        
        return jwtAuthenticationConverter;
    }
    
    /**
     * Password encoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
    
    /**
     * Authentication manager bean
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
