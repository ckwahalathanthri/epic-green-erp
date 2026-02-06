package lk.epicgreen.erp.admin.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import lk.epicgreen.erp.common.config.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
@Slf4j
public class OAuth2Controller {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Value("${application.security.jwt.token-expiration}")
    private long jwtExpirationMs;

    // Inject the Client Credentials
    @Value("${application.security.oauth2.client.id}")
    private String clientId;

    @Value("${application.security.oauth2.client.secret}")
    private String clientSecret;

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Map<String, Object>> getToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader,
            @RequestParam Map<String, String> parameters){

                // 1. Validate Client (Basic Auth)
        if (!isClientAuthenticated(authHeader)) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "unauthorized_client");
            error.put("error_description", "Invalid or missing client credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
        String grantType = parameters.get("grant_type");

        if("password".equals(grantType)){
            return handlePasswordGrant(parameters);
        }else if("refresh_token".equals(grantType)){
            return handleRefreshTokenGrant(parameters);
        }

        Map<String, Object> error = new HashMap<>();
        error.put("error", "unsupported_grant_type");
        error.put("error_description", "Unsupported grant type: " + grantType);
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * helper method to validate Basic Auth header against configured Client ID/Secret
     */
    private boolean isClientAuthenticated(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return false;
        }
        try {
            // Remove "Basic " prefix
            String base64Credentials = authHeader.substring(6);
            // Decode Base64
            byte[] decoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(decoded, StandardCharsets.UTF_8);
            // Split "clientId:clientSecret"
            String[] parts = credentials.split(":", 2);
            
            // Check if matches
            return parts.length == 2 && 
                   clientId.equals(parts[0]) && 
                   clientSecret.equals(parts[1]);
        } catch (IllegalArgumentException e) {
            log.error("Failed to decode Basic Auth header", e);
            return false;
        }
    }

    private ResponseEntity<Map<String, Object>> handlePasswordGrant(Map<String, String> parameters) {
        String username = parameters.get("username");
        String password = parameters.get("password");

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return ResponseEntity.ok(generateOAuthResponse(userDetails));
        }catch (Exception e) {
            log.error("Authentication failed for user: {}", username, e);
            Map<String, Object> error = new HashMap<>();
            error.put("error", "invalid_grant");
            error.put("error_description", "Bad credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    private ResponseEntity<Map<String, Object>> handleRefreshTokenGrant(Map<String, String> parameters) {
        String refreshToken = parameters.get("refresh_token");

        try{
            String username = jwtService.extractUserName(refreshToken);
            if(username != null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if(jwtService.isTokenValid(refreshToken, userDetails)){
                    return ResponseEntity.ok(generateOAuthResponse(userDetails));
                }
            }
        } catch (Exception e) {
            log.error("Refresh token validation failed", e);
        }

        Map<String, Object> error = new HashMap<>();
        error.put("error", "invalid_grant");
        error.put("error_description", "Invalid refresh token");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    private Map<String, Object> generateOAuthResponse(UserDetails userDetails){
        String accessToken = jwtService.generateToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

         String scope = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Map<String, Object> response = new HashMap<>();
        response.put("access_token", accessToken);
        response.put("token_type", "Bearer");
        response.put("expires_in", jwtExpirationMs / 1000); // Seconds
        response.put("refresh_token", refreshToken);
response.put("scope", scope);

        return response;

    }
}
