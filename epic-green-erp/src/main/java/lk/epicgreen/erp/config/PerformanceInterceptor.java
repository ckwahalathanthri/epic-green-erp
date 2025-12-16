package lk.epicgreen.erp.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Performance Monitoring Interceptor
 * Monitors and logs request execution time
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Slf4j
public class PerformanceInterceptor implements HandlerInterceptor {
    
    private static final String START_TIME_ATTRIBUTE = "startTime";
    private static final long SLOW_REQUEST_THRESHOLD_MS = 1000; // 1 second
    
    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) {
        
        request.setAttribute(START_TIME_ATTRIBUTE, System.currentTimeMillis());
        return true;
    }
    
    @Override
    public void postHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler,
            ModelAndView modelAndView) {
        
        Long startTime = (Long) request.getAttribute(START_TIME_ATTRIBUTE);
        if (startTime != null) {
            long executionTime = System.currentTimeMillis() - startTime;
            
            String method = request.getMethod();
            String uri = request.getRequestURI();
            int status = response.getStatus();
            
            if (executionTime > SLOW_REQUEST_THRESHOLD_MS) {
                log.warn("SLOW REQUEST: {} {} | Status: {} | Time: {}ms", 
                    method, uri, status, executionTime);
            } else {
                log.debug("Request completed: {} {} | Status: {} | Time: {}ms", 
                    method, uri, status, executionTime);
            }
        }
    }
}
