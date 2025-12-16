package lk.epicgreen.erp.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

/**
 * OAuth2 and JWT configuration
 * Configures JWT token generation and validation
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Configuration
@RequiredArgsConstructor
public class OAuth2Config {
    
    @Value("${security.jwt.issuer:https://epicgreen.lk}")
    private String jwtIssuer;
    
    @Value("${security.jwt.access-token-expiration:3600}")
    private long accessTokenExpiration;
    
    @Value("${security.jwt.refresh-token-expiration:86400}")
    private long refreshTokenExpiration;
    
    /**
     * JWT Decoder for validating incoming tokens
     */
    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey publicKey) {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(publicKey).build();
        
        // Configure token validators
        jwtDecoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
            new JwtTimestampValidator(Duration.ofSeconds(60)),
            new JwtIssuerValidator(jwtIssuer)
        ));
        
        return jwtDecoder;
    }
    
    /**
     * JWT Encoder for generating tokens
     */
    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }
    
    /**
     * JWK Source - JSON Web Key source for token signing
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource(KeyPair keyPair) {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build();
        
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }
    
    /**
     * RSA Key Pair for JWT signing
     * In production, load from secure storage
     */
    @Bean
    public KeyPair keyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to generate RSA key pair", e);
        }
    }
    
    /**
     * RSA Public Key
     */
    @Bean
    public RSAPublicKey publicKey(KeyPair keyPair) {
        return (RSAPublicKey) keyPair.getPublic();
    }
    
    /**
     * RSA Private Key
     */
    @Bean
    public RSAPrivateKey privateKey(KeyPair keyPair) {
        return (RSAPrivateKey) keyPair.getPrivate();
    }
    
    /**
     * Access token expiration in seconds
     */
    @Bean("accessTokenExpiration")
    public long accessTokenExpiration() {
        return accessTokenExpiration;
    }
    
    /**
     * Refresh token expiration in seconds
     */
    @Bean("refreshTokenExpiration")
    public long refreshTokenExpiration() {
        return refreshTokenExpiration;
    }
    
    /**
     * JWT Issuer
     */
    @Bean("jwtIssuer")
    public String jwtIssuer() {
        return jwtIssuer;
    }
}
