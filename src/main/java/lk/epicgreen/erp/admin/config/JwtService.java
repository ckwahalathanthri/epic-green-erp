package lk.epicgreen.erp.admin.config;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    @Value("${application.security.jwt.issuer:epic-green-erp}")
    private String issuer;
    
    // @Value("${application.security.jwt.secret-key:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    // private String secretKey;

    @Value("${application.security.jwt.token-expiration:864000000}")//1 day
    private long jwtExpirationMs;

    @Value("${application.security.jwt.refresh-token-expiration:604800000}")//7 days
    private long refreshExpirationMs;

    // public String extractUserName(String token) {
    //     return extractClaim(token, Claims::getSubject);
    // }

    public String extractUserName(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getSubject();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, jwtExpirationMs);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, refreshExpirationMs);
    }

    private String generateToken(UserDetails userDetails, long expirationMs) {
        Instant now = Instant.now();
        
        // Extract roles/authorities from UserDetails
        String authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plus(expirationMs, ChronoUnit.MILLIS))
                .subject(userDetails.getUsername())
                .claim("authorities", authorities)
                .claim("scope", authorities) // OAuth2 standard claim
                .build();
        
        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUserName(token);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        return jwt.getExpiresAt() != null && jwt.getExpiresAt().isBefore(Instant.now());
    }

    public Jwt decodeToken(String token) {
        return jwtDecoder.decode(token);
    }

       

    // public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    //     final Claims claims = extractAllClaims(token);
    //     return claimsResolver.apply(claims);
    // }

    // public String generateToken(UserDetails userDetails){
    //     return generateToken(new HashMap<>(), userDetails);
    // }

    // public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
    //     return createToken(extraClaims, userDetails, jwtExpirationMs);
    // }

    // public String generateRefreshToken(UserDetails userDetails){
    //     return createToken(new HashMap<>(), userDetails, refreshExpirationMs);
    // }

    // private String createToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration){
    //      return Jwts.builder()
    //             .claims(extraClaims)
    //             .subject(userDetails.getUsername())
    //             .issuedAt(new Date(System.currentTimeMillis()))
    //             .expiration(new Date(System.currentTimeMillis() + expiration))
    //             .signWith(getSignInKey())
    //             .compact();
    // }

    // public boolean isTokenValid(String token, UserDetails userDetails) {
    //     final String username = extractUserName(token);
    //     return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    // }

    // private boolean isTokenExpired(String token) {
    //     return extractExpiration(token).before(new Date());
    // }

    // private Date extractExpiration(String token) {
    //     return extractClaim(token, Claims::getExpiration);
    // }

    // private Claims extractAllClaims(String token) {
    //     return Jwts.parser()
    //             .verifyWith(getSignInKey())
    //             .build()
    //             .parseSignedClaims(token)
    //             .getPayload();
    // }

    // private SecretKey  getSignInKey() {
    //     byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    //     return Keys.hmacShaKeyFor(keyBytes);
    // }
}
