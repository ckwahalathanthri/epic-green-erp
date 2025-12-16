package lk.epicgreen.erp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lk.epicgreen.erp.common.constants.AppConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis configuration for caching
 * Configures Redis connection, cache manager, and cache settings
 * 
 * @author Epic Green Development Team
 * @version 1.0
 */
@Configuration
@EnableCaching
public class RedisConfig implements CachingConfigurer {
    
    @Value("${spring.redis.host:localhost}")
    private String redisHost;
    
    @Value("${spring.redis.port:6379}")
    private int redisPort;
    
    @Value("${spring.redis.password:}")
    private String redisPassword;
    
    @Value("${spring.redis.database:0}")
    private int redisDatabase;
    
    /**
     * Redis connection factory
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);
        config.setDatabase(redisDatabase);
        
        if (redisPassword != null && !redisPassword.isEmpty()) {
            config.setPassword(redisPassword);
        }
        
        return new LettuceConnectionFactory(config);
    }
    
    /**
     * Redis template for generic operations
     */
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // Use String serializer for keys
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        
        // Use JSON serializer for values
        GenericJackson2JsonRedisSerializer jsonSerializer = 
            new GenericJackson2JsonRedisSerializer(redisObjectMapper());
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer);
        
        template.afterPropertiesSet();
        return template;
    }
    
    /**
     * Cache manager with custom cache configurations
     */
    @Bean
    @Override
    public CacheManager cacheManager() {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofSeconds(AppConstants.CACHE_TTL_MEDIUM))
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
            )
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer(redisObjectMapper())
                )
            )
            .disableCachingNullValues();
        
        // Custom cache configurations for specific caches
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        
        // Products cache - 30 minutes
        cacheConfigurations.put(
            AppConstants.CACHE_PRODUCTS,
            defaultConfig.entryTtl(Duration.ofSeconds(AppConstants.CACHE_TTL_MEDIUM))
        );
        
        // Categories cache - 1 hour
        cacheConfigurations.put(
            AppConstants.CACHE_CATEGORIES,
            defaultConfig.entryTtl(Duration.ofSeconds(AppConstants.CACHE_TTL_LONG))
        );
        
        // Users cache - 15 minutes
        cacheConfigurations.put(
            AppConstants.CACHE_USERS,
            defaultConfig.entryTtl(Duration.ofSeconds(AppConstants.CACHE_TTL_SHORT))
        );
        
        // Settings cache - 1 hour
        cacheConfigurations.put(
            AppConstants.CACHE_SETTINGS,
            defaultConfig.entryTtl(Duration.ofSeconds(AppConstants.CACHE_TTL_LONG))
        );
        
        // Suppliers cache - 30 minutes
        cacheConfigurations.put(
            AppConstants.CACHE_SUPPLIERS,
            defaultConfig.entryTtl(Duration.ofSeconds(AppConstants.CACHE_TTL_MEDIUM))
        );
        
        // Customers cache - 30 minutes
        cacheConfigurations.put(
            AppConstants.CACHE_CUSTOMERS,
            defaultConfig.entryTtl(Duration.ofSeconds(AppConstants.CACHE_TTL_MEDIUM))
        );
        
        // Inventory cache - 5 minutes (frequently changing)
        cacheConfigurations.put(
            AppConstants.CACHE_INVENTORY,
            defaultConfig.entryTtl(Duration.ofSeconds(AppConstants.CACHE_TTL_SHORT))
        );
        
        // Reports cache - 10 minutes
        cacheConfigurations.put(
            AppConstants.CACHE_REPORTS,
            defaultConfig.entryTtl(Duration.ofSeconds(600))
        );
        
        return RedisCacheManager.builder(redisConnectionFactory())
            .cacheDefaults(defaultConfig)
            .withInitialCacheConfigurations(cacheConfigurations)
            .transactionAware()
            .build();
    }
    
    /**
     * Custom key generator for cache keys
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getSimpleName());
            sb.append(".");
            sb.append(method.getName());
            sb.append(":");
            
            for (Object param : params) {
                if (param != null) {
                    sb.append(param.toString());
                    sb.append(",");
                }
            }
            
            // Remove trailing comma
            if (sb.charAt(sb.length() - 1) == ',') {
                sb.setLength(sb.length() - 1);
            }
            
            return sb.toString();
        };
    }
    
    /**
     * Cache error handler - prevents cache failures from breaking the application
     */
    @Bean
    @Override
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException exception, 
                                           org.springframework.cache.Cache cache, 
                                           Object key) {
                // Log error but don't fail
                System.err.println("Cache GET error: " + exception.getMessage());
                super.handleCacheGetError(exception, cache, key);
            }
            
            @Override
            public void handleCachePutError(RuntimeException exception, 
                                           org.springframework.cache.Cache cache, 
                                           Object key, 
                                           Object value) {
                // Log error but don't fail
                System.err.println("Cache PUT error: " + exception.getMessage());
                super.handleCachePutError(exception, cache, key, value);
            }
            
            @Override
            public void handleCacheEvictError(RuntimeException exception, 
                                             org.springframework.cache.Cache cache, 
                                             Object key) {
                // Log error but don't fail
                System.err.println("Cache EVICT error: " + exception.getMessage());
                super.handleCacheEvictError(exception, cache, key);
            }
            
            @Override
            public void handleCacheClearError(RuntimeException exception, 
                                             org.springframework.cache.Cache cache) {
                // Log error but don't fail
                System.err.println("Cache CLEAR error: " + exception.getMessage());
                super.handleCacheClearError(exception, cache);
            }
        };
    }
    
    /**
     * ObjectMapper for Redis JSON serialization
     */
    private ObjectMapper redisObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.activateDefaultTyping(
            mapper.getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL
        );
        return mapper;
    }
}
