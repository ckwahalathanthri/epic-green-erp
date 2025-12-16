package lk.epicgreen.erp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.epicgreen.erp.common.dto.ApiResponse;
import lk.epicgreen.erp.common.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * JWT Authentication Entry Point
 * Handles authentication failures and returns proper JSON response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    private final ObjectMapper objectMapper;
    
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        
        log.error("Authentication error: {} - {}", authException.getMessage(), request.getRequestURI());
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.UNAUTHORIZED.value())
            .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
            .message("Authentication failed: " + authException.getMessage())
            .path(request.getRequestURI())
            .build();
        
        ApiResponse<ErrorResponse> apiResponse = ApiResponse.error(errorResponse.getMessage(), errorResponse.getStatus(), errorResponse.getPath());
        
        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }
}
