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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * JWT Access Denied Handler
 * Handles authorization failures and returns proper JSON response
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    
    private final ObjectMapper objectMapper;
    
    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        
        log.error("Access denied: {} - {}", accessDeniedException.getMessage(), request.getRequestURI());
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.FORBIDDEN.value())
            .error(HttpStatus.FORBIDDEN.getReasonPhrase())
            .message("Access denied: " + accessDeniedException.getMessage())
            .path(request.getRequestURI())
            .build();

        ApiResponse<ErrorResponse> apiResponse = ApiResponse.error(errorResponse.getMessage(), errorResponse.getStatus(), errorResponse.getPath());
        
        objectMapper.writeValue(response.getOutputStream(), apiResponse);
    }
}
