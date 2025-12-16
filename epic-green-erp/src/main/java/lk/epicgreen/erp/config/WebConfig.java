package lk.epicgreen.erp.config;

import lk.epicgreen.erp.common.constants.AppConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Web MVC configuration
 * Configures CORS, interceptors, formatters, and message converters
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    
    /**
     * Configure CORS mappings
     * Note: This works with SecurityConfig CORS configuration
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins(
                "http://localhost:4200",
                "http://localhost:3000",
                "https://epicgreen.lk",
                "https://www.epicgreen.lk"
            )
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("Authorization", "X-Total-Count", "X-Page-Number", "X-Page-Size")
            .allowCredentials(true)
            .maxAge(3600);
    }
    
    /**
     * Configure resource handlers for static content
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Swagger UI
        registry.addResourceHandler("/swagger-ui/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/springdoc-openapi-ui/")
            .resourceChain(false);
        
        // Static resources
        registry.addResourceHandler("/static/**")
            .addResourceLocations("classpath:/static/")
            .setCachePeriod(86400); // 1 day cache
        
        // File uploads
        registry.addResourceHandler("/uploads/**")
            .addResourceLocations("file:" + AppConstants.UPLOAD_DIR + "/")
            .setCachePeriod(3600); // 1 hour cache
    }
    
    /**
     * Add interceptors
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Request logging interceptor
        registry.addInterceptor(new RequestLoggingInterceptor())
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/v1/auth/**");
        
        // Performance monitoring interceptor
        registry.addInterceptor(new PerformanceInterceptor())
            .addPathPatterns("/api/**");
    }
    
    /**
     * Configure path matching
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
//        configurer
//            .setUseTrailingSlashMatch(false)
//            .setUseCaseSensitiveMatch(true);
    }
    
    /**
     * Configure content negotiation
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
            .favorParameter(false)
            .ignoreAcceptHeader(false)
            .defaultContentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .mediaType("json", org.springframework.http.MediaType.APPLICATION_JSON)
            .mediaType("xml", org.springframework.http.MediaType.APPLICATION_XML);
    }
    
    /**
     * Configure formatters for date/time conversion
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT));
        registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern(AppConstants.DATETIME_FORMAT));
        registrar.registerFormatters(registry);
    }
    
    /**
     * Configure message converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(objectMapper()));
    }
    
    /**
     * Configure Jackson ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json()
            .modules(new JavaTimeModule())
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .featuresToEnable(SerializationFeature.INDENT_OUTPUT)
            .build();
    }
    
    /**
     * Configure view resolvers
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/", ".jsp");
    }
    
    /**
     * Configure default servlet handling
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
